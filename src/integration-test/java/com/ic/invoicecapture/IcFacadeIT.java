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

  private MockServerFacade mockServer;

  @BeforeEach
  private void startServer() {
    mockServer = new MockServerFacade();
  }

  @AfterEach
  private void closeServer() throws IOException {
    mockServer.close();
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

    IBuilder<MockResponse, String> mockBuilder = (companyJson) -> new MockResponse()
        .setHeader("Content-Type", "application/json").setBody(companyJson);
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


  private void assertReceivedCorrectJson(IcFacade icFacade, Company correctCompany)
      throws IcException {
    Company returnedCompany = icFacade.requestCompanyInfo();
    Assertions.assertEquals(correctCompany, returnedCompany);
  }

  @Test
  public void requestCompanyInfo_successNormalConditions()
      throws IcException, IOException, InterruptedException {
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration();
    Company correctCompany = pair.getValue1();
    this.mockServer.addMockResponse(pair.getValue0());

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUri);
    this.assertReceivedCorrectJson(icFacade, correctCompany);

    this.assertSentCorrectGet(IcFacade.COMPANIES_ENDPOINT, baseUri);
  }

  @Test
  public void requestCompanyInfo_extraMessageSuffix()
      throws IcException, IOException, InterruptedException {
    String extraMessage = CompanyBuilder.buildTestCompanyBuilder().setCity("/").setGid("//")
        .buildJsonObject().toString();

    IBuilder<MockResponse, String> mockBuilder = (companyJson) -> new MockResponse()
        .setHeader("Content-Type", "application/json").setBody(companyJson + "\n" + extraMessage);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    Company correctCompany = pair.getValue1();
    this.mockServer.addMockResponse(pair.getValue0());

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUri);
    this.assertReceivedCorrectJson(icFacade, correctCompany);

    this.assertSentCorrectGet(IcFacade.COMPANIES_ENDPOINT, baseUri);
  }

  // should only have one of these
  @Test
  public void requestCompanyInfo_slowConnection()
      throws IOException, IcException, InterruptedException {
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration();
    MockResponse response = pair.getValue0();
    response.throttleBody(1, 1, TimeUnit.MILLISECONDS); // 1000 Byte/sec
    this.mockServer.addMockResponse(pair.getValue0());

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUri);
    Company correctCompany = pair.getValue1();
    this.assertReceivedCorrectJson(icFacade, correctCompany);

    this.assertSentCorrectGet(IcFacade.COMPANIES_ENDPOINT, baseUri);
  }

  @Test
  public void requestCompanyInfo_unparsableJson()
      throws IOException, IcException, InterruptedException {

    String badJson = "{231,,[][[";

    IBuilder<MockResponse, String> mockBuilder = (companyJson) -> new MockResponse()
        .setHeader("Content-Type", "application/json").setBody(badJson);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    this.mockServer.addMockResponse(pair.getValue0());

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUri);
    Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
  }

  @Test
  public void requestCompanyInfo_failOnNoContentTypeHeader()
      throws IOException, IcException, InterruptedException {
    IBuilder<MockResponse, String> mockBuilder =
        (companyJson) -> new MockResponse().setBody(companyJson);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    this.mockServer.addMockResponse(pair.getValue0());

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUri);
    Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
  }

  @Test
  public void requestCompanyInfo_erroredStatusCode()
      throws IOException, IcException, InterruptedException {
    IBuilder<MockResponse, String> mockBuilder =
        (companyJson) -> new MockResponse().setResponseCode(400).setBody(companyJson);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    this.mockServer.addMockResponse(pair.getValue0());

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUri);
    Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
  }

  private static final String REDIRECT_URL = "redirect";

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
    this.assertReceivedCorrectJson(icFacade, correctCompany);
    this.assertSentCorrectGet(IcFacade.COMPANIES_ENDPOINT, connectionUrl);
    this.assertSentCorrectGet(REDIRECT_URL, connectionUrl);
  }

  @Test
  public void updateCompanyInfo() throws Exception {
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration();
    this.mockServer.addMockResponse(pair.getValue0());

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    Company companyUpdate = CompanyBuilder.buildTestCompanyBuilder().buildCompany();
    companyUpdate.setAddress("new address");
    companyUpdate.setCity("new city");
    IcFacade icFacade = new IcFacade(TEST_API_TOKEN, baseUri);
    Company returnedCompany = icFacade.updateCompanyInfo(companyUpdate);
    Company companyToReceive = pair.getValue1();
    Assertions.assertEquals(companyToReceive, returnedCompany);

    this.assertSentCorrectPut(IcFacade.COMPANIES_ENDPOINT, baseUri);
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

}
