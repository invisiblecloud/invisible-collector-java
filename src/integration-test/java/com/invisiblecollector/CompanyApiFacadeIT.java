package com.invisiblecollector;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.invisiblecollector.connection.RequestType;
import com.invisiblecollector.connection.builders.IBuilder;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Company;
import com.invisiblecollector.model.builder.CompanyBuilder;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

class CompanyApiFacadeIT extends IcFacadeTestBase {

  private static final String REDIRECT_URL = "redirect";
  private static final String COMPANIES_ENDPOINT = "companies";
  private static final String DISABLE_NOTIFICATIONS_ENDPOINT = "companies/disableNotifications";
  private static final String ENABLE_NOTIFICATIONS_ENDPOINT = "companies/enableNotifications";
  
  private Pair<MockResponse, Company> buildCompanyConfiguration(CompanyBuilder companyBuilder) {
    String companyJson = companyBuilder.buildJsonObject().toString();
    MockResponse mockResponse = buildBodiedMockResponse(companyJson);
    Company correctCompany = companyBuilder.buildModel();
    return Pair.with(mockResponse, correctCompany);
  }

  private Pair<MockResponse, Company> buildCompanyConfiguration(
      IBuilder<MockResponse, String> mockBuilder) {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    String companyJson = companyBuilder.buildJsonObject().toString();
    MockResponse mockResponse = mockBuilder.build(companyJson);
    Company correctCompany = companyBuilder.buildModel();
    return Pair.with(mockResponse, correctCompany);
  }

  private Pair<MockResponse, Company> buildCompanyConfiguration() {

    IBuilder<MockResponse, String> mockBuilder =
        (companyJson) -> buildBodiedMockResponse(companyJson);
    return this.buildCompanyConfiguration(mockBuilder);
  }

  private void assertRequestWithReturnedCompany(Pair<MockResponse, Company> pair,
      IThrowingBuilder2<Company, CompanyApiFacade, Company> facadeMethod) throws Exception {

    CompanyApiFacade icFacade = initMockServer(pair.getValue0()).getCompanyFacade();
    Company returnedCompany = facadeMethod.build(icFacade, pair.getValue1());
    Company companyToReceive = pair.getValue1();
    Assertions.assertEquals(companyToReceive, returnedCompany);
  }

  private void assertRequestWithReturnedCompany(CompanyBuilder companyBuilder,
      IThrowingBuilder2<Company, CompanyApiFacade, Company> facadeMethod) throws Exception {
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(companyBuilder);
    this.assertRequestWithReturnedCompany(pair, facadeMethod);
  }

  @Test
  public void requestCompanyInfo_successNormalConditions() throws Exception {
    this.assertRequestWithReturnedCompany(CompanyBuilder.buildTestCompanyBuilder(),
        (icFacade, company) -> icFacade.requestCompanyInfo());

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, COMPANIES_ENDPOINT,
        this.mockServer.getBaseUri(), RequestType.GET);
  }

  @Test
  public void requestCompanyInfo_extraMessageSuffix() throws Exception {
    String extraMessage = CompanyBuilder.buildTestCompanyBuilder().setCity("/").setGid("//")
        .buildJsonObject().toString();
    IBuilder<MockResponse, String> mockBuilder = (companyJson) -> new MockResponse()
        .setHeader("Content-Type", "application/json").setBody(companyJson + "\n" + extraMessage);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    this.assertRequestWithReturnedCompany(pair,
        (icFacade, company) -> icFacade.requestCompanyInfo());

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, COMPANIES_ENDPOINT,
        this.mockServer.getBaseUri(), RequestType.GET);
  }

  // should only have one of these
  @Test
  public void requestCompanyInfo_slowConnection() throws Exception {
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration();
    MockResponse response = pair.getValue0();
    response.throttleBody(1, 1, TimeUnit.MILLISECONDS); // 1000 Byte/sec
    this.assertRequestWithReturnedCompany(pair,
        (icFacade, company) -> icFacade.requestCompanyInfo());

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, COMPANIES_ENDPOINT,
        this.mockServer.getBaseUri(), RequestType.GET);
  }

  @Test
  public void requestCompanyInfo_unparsableJson() throws Exception {

    String badJson = "{231,,[][[";

    MockResponse mockResponse =
        new MockResponse().setHeader("Content-Type", "application/json").setBody(badJson);
    CompanyApiFacade icFacade = initMockServer(mockResponse).getCompanyFacade();
    Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
  }

  @Test
  public void requestCompanyInfo_failOnNoContentTypeHeader() throws Exception {
    IBuilder<MockResponse, String> mockBuilder =
        (companyJson) -> new MockResponse().setBody(companyJson);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(mockBuilder);
    CompanyApiFacade icFacade = initMockServer(pair.getValue0()).getCompanyFacade();
    Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
  }

  @Test
  public void requestCompanyInfo_erroredStatusCode() throws Exception {
    int statusCode = 400;
    String json = buildErrorJson(statusCode);
    MockResponse mockResponse = buildBodiedMockResponse(json).setResponseCode(statusCode);
    CompanyApiFacade icFacade = initMockServer(mockResponse).getCompanyFacade();
    IcException exception =
        Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
    Assertions.assertTrue(exception.getMessage().contains(JSON_ERROR_MESSAGE));
  }

  @Test
  public void requestCompanyInfo_followRedirect() throws Exception {
    URI connectionUrl = this.mockServer.getBaseUri();
    String redirectUrl = this.mockServer.getBaseUri().toString() + "/" + REDIRECT_URL;
    MockResponse response =
        new MockResponse().setHeader("Location", redirectUrl).setResponseCode(301);
    this.mockServer.addMockResponse(response);
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration();
    Company correctCompany = pair.getValue1();
    this.mockServer.addMockResponse(pair.getValue0());

    // this.mockServer.start(); //already started?
    CompanyApiFacade icFacade = new CompanyApiFacade(TEST_API_TOKEN, connectionUrl);
    Company receivedCompany = icFacade.requestCompanyInfo();
    Assertions.assertEquals(receivedCompany, correctCompany);
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, COMPANIES_ENDPOINT, connectionUrl,
        RequestType.GET);
    RecordedRequest request2 = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request2, REDIRECT_URL, connectionUrl, RequestType.GET);
  }

  @Test
  public void updateCompanyInfo_correctness() throws Exception {
    final String newAddress = StringTestUtils.randomHexString();
    final String newCity = StringTestUtils.randomHexString();

    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setAddress(newAddress);
    companyBuilder.setCity(newCity);

    assertRequestWithReturnedCompany(companyBuilder,
        (icFacade, company) -> icFacade.updateCompanyInfo(company));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeaders(request, COMPANIES_ENDPOINT,
        this.mockServer.getBaseUri(), RequestType.PUT);
    assertSentCorrectJson(request, companyBuilder.buildSendableJson());
  }

  @Test
  public void setCompanyNotifications_enable() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setNotificationsEnabled(true);

    assertRequestWithReturnedCompany(companyBuilder,
        (icFacade, unused) -> icFacade.setCompanyNotifications(true));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectBodylessHeaders(request, ENABLE_NOTIFICATIONS_ENDPOINT,
        this.mockServer.getBaseUri(), RequestType.PUT);
  }

  @Test
  public void setCompanyNotifications_disable() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setNotificationsEnabled(false);

    assertRequestWithReturnedCompany(companyBuilder,
        (icFacade, unused) -> icFacade.setCompanyNotifications(false));
    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectBodylessHeaders(request, DISABLE_NOTIFICATIONS_ENDPOINT,
        this.mockServer.getBaseUri(), RequestType.PUT);
  }

}
