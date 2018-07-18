package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.builders.IBuilder;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.builder.CompanyBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.MockResponse;
import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IcFacadeIT_Company extends IcFacadeTestBase {

  private static final String REDIRECT_URL = "redirect";

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

  private void assertRequestWithReturnedCompany(Pair<MockResponse, Company> pair,
      IBuilder2<Company, IcFacade, Company> facadeMethod) throws Exception {

    IcFacade icFacade = initMockServer(pair.getValue0());
    Company returnedCompany = facadeMethod.build(icFacade, pair.getValue1());
    Company companyToReceive = pair.getValue1();
    Assertions.assertEquals(companyToReceive, returnedCompany);
  }

  private void assertRequestWithReturnedCompany(CompanyBuilder companyBuilder,
      IBuilder2<Company, IcFacade, Company> facadeMethod) throws Exception {
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration(companyBuilder);
    this.assertRequestWithReturnedCompany(pair, facadeMethod);
  }

  @Test
  public void requestCompanyInfo_successNormalConditions() throws Exception {
    this.assertRequestWithReturnedCompany(CompanyBuilder.buildTestCompanyBuilder(),
        (icFacade, company) -> icFacade.requestCompanyInfo());

    this.assertSentCorrectGetHeaders(IcFacade.COMPANIES_ENDPOINT, this.mockServer.getBaseUri());
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

    this.assertSentCorrectGetHeaders(IcFacade.COMPANIES_ENDPOINT, this.mockServer.getBaseUri());
  }

  // should only have one of these
  @Test
  public void requestCompanyInfo_slowConnection() throws Exception {
    Pair<MockResponse, Company> pair = this.buildCompanyConfiguration();
    MockResponse response = pair.getValue0();
    response.throttleBody(1, 1, TimeUnit.MILLISECONDS); // 1000 Byte/sec
    this.assertRequestWithReturnedCompany(pair,
        (icFacade, company) -> icFacade.requestCompanyInfo());

    this.assertSentCorrectGetHeaders(IcFacade.COMPANIES_ENDPOINT, this.mockServer.getBaseUri());
  }

  @Test
  public void requestCompanyInfo_unparsableJson() throws Exception {

    String badJson = "{231,,[][[";

    MockResponse mockResponse =
        new MockResponse().setHeader("Content-Type", "application/json").setBody(badJson);
    IcFacade icFacade = initMockServer(mockResponse);
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
    int statusCode = 400;
    String json = buildErrorJson(statusCode);
    MockResponse mockResponse = buildBodiedMockResponse(json).setResponseCode(statusCode);
    IcFacade icFacade = initMockServer(mockResponse);
    IcException exception =
        Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
    Assertions.assertTrue(exception.getMessage().contains(JSON_ERROR_MESSAGE));
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
    this.assertSentCorrectGetHeaders(IcFacade.COMPANIES_ENDPOINT, connectionUrl);
    this.assertSentCorrectGetHeaders(REDIRECT_URL, connectionUrl);
  }

  @Test
  public void updateCompanyInfo_correctness() throws Exception {
    final String newAddress = StringUtils.randomHexString();
    final String newCity = StringUtils.randomHexString();

    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setAddress(newAddress);
    companyBuilder.setCity(newCity);

    assertRequestWithReturnedCompany(companyBuilder,
        (icFacade, company) -> icFacade.updateCompanyInfo(company));
    this.assertSentCorrectPutHeaders(IcFacade.COMPANIES_ENDPOINT, this.mockServer.getBaseUri());
  }

  @Test
  public void setCompanyNotifications_enable() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setNotificationsEnabled(true);

    assertRequestWithReturnedCompany(companyBuilder,
        (icFacade, unused) -> icFacade.setCompanyNotifications(true));
  }

  @Test
  public void setCompanyNotifications_disable() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setNotificationsEnabled(false);

    assertRequestWithReturnedCompany(companyBuilder,
        (icFacade, unused) -> icFacade.setCompanyNotifications(false));
  }

}
