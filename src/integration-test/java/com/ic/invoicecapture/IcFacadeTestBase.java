package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.RequestType;
import java.io.IOException;
import java.net.URI;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class IcFacadeTestBase {

  protected static final String CONFLICT_GID = "abdbdbd0000";
  protected static final String JSON_ERROR_MESSAGE = "error-message";
  protected static final String TEST_API_TOKEN = "1234567890abcdef";

  protected MockServerFacade mockServer;

  private void assertSentCorrectBodiedHeaders(RecordedRequest request, String endpoint, URI baseUri,
      String requestType) throws Exception {
    MockServerFacade.assertApiEndpointHit(request, endpoint);
    this.assertSentCorrectHeadersCommon(request, endpoint, baseUri, requestType);
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "utf-8");
    MockServerFacade.assertHasHeader(request, "Content-Length");

  }

  protected void assertSentCorrectGetHeaders(String endpoint, URI baseUrl)
      throws InterruptedException {

    RecordedRequest request = this.mockServer.getRequest();
    this.assertSentCorrectHeadersCommon(request, endpoint, baseUrl, "GET");
  }

  private void assertSentCorrectHeadersCommon(RecordedRequest request, String endpoint, URI baseUrl,
      String requestType) throws InterruptedException {
    MockServerFacade.assertApiEndpointHit(request, endpoint);
    MockServerFacade.assertHeaderContainsValue(request, "X-Api-Token", TEST_API_TOKEN);
    MockServerFacade.assertHeaderContainsValue(request, "Accept", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Host", baseUrl.getHost());
    MockServerFacade.assertHasHeader(request, "Date");
    MockServerFacade.assertRequestLineContains(request, requestType);
  }

  protected void assertSentCorrectPostHeaders(String endpoint, URI baseUri) throws Exception {
    RecordedRequest request = this.mockServer.getRequest();
    assertSentCorrectBodiedHeaders(request, endpoint, baseUri, "POST");
  }

  protected void assertSentCorrectPutHeaders(String endpoint, URI baseUrl) throws Exception {
    RecordedRequest request = this.mockServer.getRequest();
    assertSentCorrectBodiedHeaders(request, endpoint, baseUrl, "PUT");
  }

  protected void assertSentCorrectHeaders(String endpoint, URI baseUrl, RequestType requestType)
      throws Exception {
    switch (requestType) {
      case GET:
        assertSentCorrectGetHeaders(endpoint, baseUrl);
        break;
      case POST:
        assertSentCorrectPostHeaders(endpoint, baseUrl);
        break;
      case PUT:
        assertSentCorrectPutHeaders(endpoint, baseUrl);
        break;
      default:
        throw new IllegalArgumentException("Invalid request Type");
    }
  }

  protected MockResponse buildBodiedMockResponse(String bodyJson) {
    return new MockResponse().setHeader("Content-Type", "application/json").setBody(bodyJson);
  }

  protected String buildConflictErrorJson(int statusCode) {
    return String.format("{\"code\": %d, \"message\": %s, \"gid\": %s}", statusCode,
        JSON_ERROR_MESSAGE, CONFLICT_GID);
  }

  protected String buildErrorJson(int statusCode) {
    return String.format("{\"code\": %d, \"message\": %s}", statusCode, JSON_ERROR_MESSAGE);
  }

  @AfterEach
  private void closeServer() throws IOException {
    mockServer.close();
  }

  protected IcFacade initMockServer(MockResponse response) throws Exception {
    this.mockServer.addMockResponse(response);

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    return new IcFacade(TEST_API_TOKEN, baseUri);
  }

  protected String joinUriPaths(String... paths) {
    return String.join("/", paths);
  }

  @BeforeEach
  private void startServer() {
    mockServer = new MockServerFacade();
  }


}
