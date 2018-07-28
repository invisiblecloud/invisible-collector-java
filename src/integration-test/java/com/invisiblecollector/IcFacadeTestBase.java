package com.invisiblecollector;

import java.io.IOException;
import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.invisiblecollector.connection.RequestType;
import com.invisiblecollector.connection.builders.IThrowingBuilder;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.builder.BuilderBase;
import com.invisiblecollector.model.json.JsonTestUtils;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class IcFacadeTestBase {

  protected static final String CONFLICT_GID = "abdbdbd0000";
  protected static final String JSON_ERROR_MESSAGE = "error-message";
  protected static final String TEST_API_TOKEN = "1234567890abcdef";

  protected MockServerFacade mockServer;

  protected <T> void assertCorrectModelReturned(BuilderBase modelBuilder,
      IThrowingBuilder<T, T> method) throws IcException {
    @SuppressWarnings("unchecked")
    T correctModel = (T) modelBuilder.buildModel();
    T returnedModel = (T) method.build(correctModel);
    JsonTestUtils.assertObjectsEqualsAsJson(correctModel, returnedModel);
  }

  private void assertSentCorrectBodiedHeaders(RecordedRequest request, String endpoint, URI baseUri,
      String requestType) throws Exception {
    MockServerFacade.assertApiEndpointHit(request, endpoint);
    this.assertSentCorrectHeadersCommon(request, endpoint, baseUri, requestType);
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "utf-8");
    MockServerFacade.assertHasHeader(request, "Content-Length");

  }
  
  protected void assertSentCorrectHeaders(RecordedRequest request, String endpoint,
      URI baseUrl, RequestType requestType) throws Exception {
    switch (requestType) {
      case GET:
        this.assertSentCorrectHeadersCommon(request, endpoint, baseUrl, "GET");
        break;
      case POST:
        assertSentCorrectBodiedHeaders(request, endpoint, baseUrl, "POST");
        break;
      case PUT:
        assertSentCorrectBodiedHeaders(request, endpoint, baseUrl, "PUT");
        break;
      default:
        throw new IllegalArgumentException("Invalid request Type");
    }
  }
  
  protected void assertSentCorrectBodylessHeaders(RecordedRequest request, String endpoint,
      URI baseUrl, RequestType requestType) throws Exception {
    this.assertSentCorrectHeadersCommon(request, endpoint, baseUrl, requestType.toString());
  }


  private void assertSentCorrectHeadersCommon(RecordedRequest request, String endpoint, URI baseUrl,
      String requestType) throws InterruptedException {
    MockServerFacade.assertApiEndpointHit(request, endpoint);
    MockServerFacade.assertHeaderContainsValue(request, "Authorization", TEST_API_TOKEN);
    MockServerFacade.assertHeaderContainsValue(request, "Authorization", "Bearer");
    MockServerFacade.assertHeaderContainsValue(request, "Accept", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Host", baseUrl.getHost());
    MockServerFacade.assertHasHeader(request, "Date");
    MockServerFacade.assertRequestLineContains(request, requestType);
  }

  protected void assertSentCorrectJson(RecordedRequest request, String expectedJson) {
    String returnedJson = request.getBody().readUtf8();
    JsonTestUtils.assertJsonEquals(expectedJson, returnedJson);
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

  protected IcApiFacade buildIcApiResponseAndAddServerReply(BuilderBase modelBuilder)
      throws Exception {
    String json = modelBuilder.buildJson();
    MockResponse mockResponse = buildBodiedMockResponse(json);
    return initMockServer(mockResponse);
  }

  @AfterEach
  private void closeServer() throws IOException {
    mockServer.close();
  }

  protected IcApiFacade initMockServer(MockResponse response) throws Exception {
    this.mockServer.addMockResponse(response);

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    return new IcApiFacade(TEST_API_TOKEN, baseUri);
  }

  protected String joinUriPaths(String... paths) {
    return String.join("/", paths);
  }

  @BeforeEach
  private void startServer() {
    mockServer = new MockServerFacade();
  }


}
