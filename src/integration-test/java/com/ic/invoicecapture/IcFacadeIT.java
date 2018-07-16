package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.builders.IBuilder;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.builder.CompanyBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcFacadeIT {

  private static final String TEST_API_TOKEN = "1234567890abcdef";
  private static final String REDIRECT_URL = "redirect";

  private MockServerFacade mockServer;

  @BeforeEach
  private void startServer() {
    mockServer = new MockServerFacade();
  }

  @AfterEach
  private void closeServer() throws IOException {
    mockServer.close();
  }

  private MockResponse buildBodiedMockResponse(String bodyJson) {
    return new MockResponse().setHeader("Content-Type", "application/json").setBody(bodyJson);
  }

  private Pair<MockResponse, Company> buildCompanyConfiguration(CompanyBuilder companyBuilder) {
    String companyJson = companyBuilder.buildJsonObject().toString();
    MockResponse mockResponse = buildBodiedMockResponse(companyJson);
    Company correctCompany = companyBuilder.buildCompany();
    return Pair.with(mockResponse, correctCompany);
  }

  private Pair<MockResponse, Company> buildCompanyConfiguration(
      IBuilder<MockResponse, String> mockBuilder) {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    String companyJson = companyBuilder.buildJsonObject().toString();
    MockResponse mockResponse = mockBuilder.build(companyJson);
    Company correctCompany = companyBuilder.buildCompany();
    return Pair.with(mockResponse, correctCompany);
  }

  private Pair<MockResponse, Company> buildCompanyConfiguration() {

    IBuilder<MockResponse, String> mockBuilder =
        (companyJson) -> buildBodiedMockResponse(companyJson);
    return this.buildCompanyConfiguration(mockBuilder);
  }

  private void assertSentCorrectGet(String endpoint, URI baseUrl) throws InterruptedException {

    RecordedRequest request = this.mockServer.getRequest();
    MockServerFacade.assertApiEndpointHit(request, endpoint);
    MockServerFacade.assertRequestLineContains(request, "GET");
    MockServerFacade.assertHeaderContainsValue(request, "X-Api-Token", TEST_API_TOKEN);
    MockServerFacade.assertHeaderContainsValue(request, "Accept", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Host", baseUrl.getHost());
    MockServerFacade.assertHasHeader(request, "Date");

  }

  private void assertSentCorrectPut(String endpoint, URI baseUrl) throws Exception {
    RecordedRequest request = this.mockServer.getRequest();
    MockServerFacade.assertApiEndpointHit(request, endpoint);
    MockServerFacade.assertRequestLineContains(request, "PUT");
    MockServerFacade.assertHeaderContainsValue(request, "X-Api-Token", TEST_API_TOKEN);
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "utf-8");
    MockServerFacade.assertHeaderContainsValue(request, "Accept", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Host", baseUrl.getHost());
    MockServerFacade.assertHasHeader(request, "Date");
    MockServerFacade.assertHasHeader(request, "Content-Length");
  }

  private void assertRequestWithReturn(Pair<MockResponse, Company> pair,
      IBuilder2<Company, IcFacade, Company> facadeMethod) throws Exception {

    IcFacade icFacade = initMockServer(pair.getValue0());
    Company returnedCompany = facadeMethod.build(icFacade, pair.getValue1());
    Company companyToReceive = pair.getValue1();
    Assertions.assertEquals(companyToReceive, returnedCompany);
  }

  private void assertRequestWithReturn(CompanyBuilder companyBuilder,
      IBuilder2<Company, IcFacade, Company> facadeMethod) throws Exception {
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(companyBuilder);
    this.assertRequestWithReturn(pair, facadeMethod);
  }

  private IcFacade initMockServer(MockResponse response) throws Exception {
    this.mockServer.addMockResponse(response);

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    return new IcFacade(TEST_API_TOKEN, baseUri);
  }

  @Test
  public void requestCompanyInfo_successNormalConditions() throws Exception {
    this.assertRequestWithReturn(CompanyBuilder.buildTestCompanyBuilder(),
        (icFacade, company) -> icFacade.requestCompanyInfo());

    this.assertSentCorrectGet(IcFacade.COMPANIES_ENDPOINT, this.mockServer.getBaseUri());
  }

  @Test
  public void requestCompanyInfo_extraMessageSuffix() throws Exception {
    String extraMessage = CompanyBuilder.buildTestCompanyBuilder().setCity("/").setGid("//")
        .buildJsonObject().toString();
    IBuilder<MockResponse, String> mockBuilder = (companyJson) -> new MockResponse()
        .setHeader("Content-Type", "application/json").setBody(companyJson + "\n" + extraMessage);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    this.assertRequestWithReturn(pair, (icFacade, company) -> icFacade.requestCompanyInfo());

    this.assertSentCorrectGet(IcFacade.COMPANIES_ENDPOINT, this.mockServer.getBaseUri());
  }

  // should only have one of these
  @Test
  public void requestCompanyInfo_slowConnection() throws Exception {
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration();
    MockResponse response = pair.getValue0();
    response.throttleBody(1, 1, TimeUnit.MILLISECONDS); // 1000 Byte/sec
    this.assertRequestWithReturn(pair, (icFacade, company) -> icFacade.requestCompanyInfo());

    this.assertSentCorrectGet(IcFacade.COMPANIES_ENDPOINT, this.mockServer.getBaseUri());
  }

  @Test
  public void requestCompanyInfo_unparsableJson() throws Exception {

    String badJson = "{231,,[][[";

    IBuilder<MockResponse, String> mockBuilder = (companyJson) -> new MockResponse()
        .setHeader("Content-Type", "application/json").setBody(badJson);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    IcFacade icFacade = initMockServer(pair.getValue0());
    Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
  }

  @Test
  public void requestCompanyInfo_failOnNoContentTypeHeader() throws Exception {
    IBuilder<MockResponse, String> mockBuilder =
        (companyJson) -> new MockResponse().setBody(companyJson);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    IcFacade icFacade = initMockServer(pair.getValue0());
    Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
  }

  @Test
  public void requestCompanyInfo_erroredStatusCode() throws Exception {
    IBuilder<MockResponse, String> mockBuilder =
        (companyJson) -> new MockResponse().setResponseCode(400).setBody(companyJson);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    IcFacade icFacade = initMockServer(pair.getValue0());
    Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
  }

  @Test
  public void requestCompanyInfo_followRedirect()
      throws IOException, IcException, InterruptedException {
    URI connectionUrl = this.mockServer.getBaseUri();
    String redirectUrl = this.mockServer.getBaseUri().toString() + "/" + REDIRECT_URL;
    MockResponse response =
        new MockResponse().setHeader("Location", redirectUrl).setResponseCode(301);
    this.mockServer.addMockResponse(response);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration();
    Company correctCompany = pair.getValue1();
    this.mockServer.addMockResponse(pair.getValue0());

    // this.mockServer.start(); //already started?
    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, connectionUrl);
    Company receivedCompany = icFacade.requestCompanyInfo();
    Assertions.assertEquals(receivedCompany, correctCompany);
    this.assertSentCorrectGet(IcFacade.COMPANIES_ENDPOINT, connectionUrl);
    this.assertSentCorrectGet(REDIRECT_URL, connectionUrl);
  }

  @Test
  public void updateCompanyInfo_correctness() throws Exception {
    final String newAddress = StringUtils.randomHexString();
    final String newCity = StringUtils.randomHexString();

    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setAddress(newAddress);
    companyBuilder.setCity(newCity);

    assertRequestWithReturn(companyBuilder,
        (icFacade, company) -> icFacade.updateCompanyInfo(company));
    this.assertSentCorrectPut(IcFacade.COMPANIES_ENDPOINT, this.mockServer.getBaseUri());
  }

  @Test
  public void setCompanyNotifications_enable() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setNotificationsEnabled(true);

    assertRequestWithReturn(companyBuilder,
        (icFacade, unused) -> icFacade.setCompanyNotifications(true));
  }

  @Test
  public void setCompanyNotifications_disable() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setNotificationsEnabled(false);

    assertRequestWithReturn(companyBuilder,
        (icFacade, unused) -> icFacade.setCompanyNotifications(false));
  }

}
