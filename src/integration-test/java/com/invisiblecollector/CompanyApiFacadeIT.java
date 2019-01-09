package com.invisiblecollector;

import com.invisiblecollector.connection.RequestType;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Company;
import com.invisiblecollector.model.builder.CompanyBuilder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.concurrent.TimeUnit;

class CompanyApiFacadeIT extends IcFacadeTestBase {

  private static final String REDIRECT_URL = "redirect";
  private static final String COMPANIES_ENDPOINT = "companies";
  private static final String DISABLE_NOTIFICATIONS_ENDPOINT = "companies/disableNotifications";
  private static final String ENABLE_NOTIFICATIONS_ENDPOINT = "companies/enableNotifications";

  @Test
  public void requestCompanyInfo_successNormalConditions() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    IcApiFacade companyApiFacade = initJsonResponseMock(companyBuilder);

    this.assertCorrectModelReturned(
        companyBuilder, (unused) -> companyApiFacade.requestCompanyInfo());

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(
        request, COMPANIES_ENDPOINT, this.mockServer.getBaseUri(), RequestType.GET);
  }

  @Test
  public void requestCompanyInfo_extraMessageSuffix() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    String json1 = companyBuilder.buildJson();
    String json2 = CompanyBuilder.buildTestCompanyBuilder().setCity("/").setGid("//").buildJson();

    MockResponse mockResponse =
        new MockResponse()
            .setHeader("Content-Type", "application/json")
            .setBody(json1 + "\n" + json2);
    IcApiFacade companyFacade = initMockServer(mockResponse);

    this.assertCorrectModelReturned(companyBuilder, (unused) -> companyFacade.requestCompanyInfo());

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(
        request, COMPANIES_ENDPOINT, this.mockServer.getBaseUri(), RequestType.GET);
  }

  // should only have one of these
  @Test
  public void requestCompanyInfo_slowConnection() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    MockResponse response = buildBodiedJsonMockResponse(companyBuilder.buildJson());

    response.throttleBody(1, 1, TimeUnit.MILLISECONDS); // 1000 Byte/sec
    IcApiFacade companyFacade = initMockServer(response);

    this.assertCorrectModelReturned(companyBuilder, (unused) -> companyFacade.requestCompanyInfo());

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(
        request, COMPANIES_ENDPOINT, this.mockServer.getBaseUri(), RequestType.GET);
  }

  @Test
  public void requestCompanyInfo_unparsableJson() throws Exception {

    String badJson = "{231,,[][[";

    MockResponse mockResponse =
        new MockResponse().setHeader("Content-Type", "application/json").setBody(badJson);
    IcApiFacade icFacade = initMockServer(mockResponse);
    IcException ex = Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
    MatcherAssert.assertThat(ex.getMessage(), CoreMatchers.containsString("Failed to parse JSON"));
  }

  @Test
  public void requestCompanyInfo_failOnNoContentTypeHeader() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    MockResponse mockResponse = new MockResponse().setBody(companyBuilder.buildJson());
    IcApiFacade companyApiFacade = initMockServer(mockResponse);

    IcException ex =
        Assertions.assertThrows(IcException.class, companyApiFacade::requestCompanyInfo);
    MatcherAssert.assertThat(
        ex.getMessage(), CoreMatchers.containsString("Expected JSON response from server."));
  }

  @Test
  public void requestCompanyInfo_erroredStatusCode() throws Exception {
    int statusCode = 400;
    String json = buildErrorJson(statusCode);
    MockResponse mockResponse = buildBodiedJsonMockResponse(json).setResponseCode(statusCode);
    IcApiFacade icFacade = initMockServer(mockResponse);
    IcException exception =
        Assertions.assertThrows(IcException.class, icFacade::requestCompanyInfo);
    MatcherAssert.assertThat(
        exception.getMessage(), CoreMatchers.containsString(JSON_ERROR_MESSAGE));
    MatcherAssert.assertThat(exception.getMessage(), CoreMatchers.containsString("" + statusCode));
  }

  @Test
  public void requestCompanyInfo_followRedirect() throws Exception {
    URI connectionUrl = this.mockServer.getBaseUri();
    String redirectUrl = this.mockServer.getBaseUri().toString() + REDIRECT_URL;
    MockResponse response =
        new MockResponse().setHeader("Location", redirectUrl).setResponseCode(301);
    this.mockServer.addMockResponse(response);

    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();

    MockResponse mockResponse = buildBodiedJsonMockResponse(companyBuilder.buildJson());
    this.mockServer.addMockResponse(mockResponse);
    IcApiFacade companyApiFacade = new IcApiFacade(TEST_API_TOKEN, connectionUrl);

    this.assertCorrectModelReturned(
        companyBuilder, (Company company) -> companyApiFacade.requestCompanyInfo());

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(request, COMPANIES_ENDPOINT, connectionUrl, RequestType.GET);
    RecordedRequest request2 = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(request2, REDIRECT_URL, connectionUrl, RequestType.GET);
  }

  @Test
  public void updateCompanyInfo_correctness() throws Exception {
    final String newAddress = StringTestUtils.randomHexString();
    final String newCity = StringTestUtils.randomHexString();

    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setAddress(newAddress);
    companyBuilder.setCity(newCity);
    IcApiFacade companyApiFacade = initJsonResponseMock(companyBuilder);

    this.assertCorrectModelReturned(
        companyBuilder, (Company company) -> companyApiFacade.updateCompanyInfo(company));

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(
        request, COMPANIES_ENDPOINT, this.mockServer.getBaseUri(), RequestType.PUT);
    assertSentCorrectJson(
        request,
        COMPANIES_ENDPOINT,
        this.mockServer.getBaseUri(),
        RequestType.PUT,
        companyBuilder.buildSendableJson(true));
  }

  @Test
  public void setCompanyNotifications_enable() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setNotificationsEnabled(true);
    IcApiFacade companyApiFacade = initJsonResponseMock(companyBuilder);

    this.assertCorrectModelReturned(
        companyBuilder, (unused) -> companyApiFacade.setCompanyNotifications(true));

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(
        request, ENABLE_NOTIFICATIONS_ENDPOINT, this.mockServer.getBaseUri(), RequestType.PUT);
  }

  @Test
  public void setCompanyNotifications_disable() throws Exception {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    companyBuilder.setNotificationsEnabled(false);
    IcApiFacade companyApiFacade = initJsonResponseMock(companyBuilder);

    this.assertCorrectModelReturned(
        companyBuilder, (unused) -> companyApiFacade.setCompanyNotifications(false));

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectCoreHeaders(
        request, DISABLE_NOTIFICATIONS_ENDPOINT, this.mockServer.getBaseUri(), RequestType.PUT);
  }
}
