package com.invisiblecollector;

import com.invisiblecollector.connection.RequestType;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Model;
import com.invisiblecollector.model.builder.BuilderBase;
import com.invisiblecollector.model.builder.IThrowingBuilder;
import com.invisiblecollector.model.serialization.JsonTestUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class IcFacadeTestBase {

  protected static final String CONFLICT_GID = "abdbdbd0000";
  protected static final String JSON_ERROR_MESSAGE = "error-message";
  protected static final String TEST_API_TOKEN = "1234567890abcdef";

  protected MockServerFacade mockServer;

  protected void assertObjectsEquals(Object expected, Object actual) {
    Assertions.assertEquals(expected, actual);
  }

  protected <T extends Model> void assertCorrectModelReturned(
      BuilderBase modelBuilder, IThrowingBuilder<T, T> method) throws IcException {
    @SuppressWarnings("unchecked")
    T correctModel = (T) modelBuilder.buildModel();
    T returnedModel = (T) method.build(correctModel);

    Map<String, Object> expectedMap = modelBuilder.buildObject();
    Map<String, Object> actualMap = returnedModel.getFields();

    assertObjectsEquals(expectedMap, actualMap);
  }

  protected void assertSentCorrectJson(RecordedRequest request, String endpoint, URI baseUri, RequestType requestType, String expectedJson) {
    String returnedJson = request.getBody().readUtf8();
    JsonTestUtils.assertJsonEquals(expectedJson, returnedJson);
    assertSentCorrectJsonHeaders(request, endpoint, baseUri, requestType);
  }

  protected MockResponse buildBodiedJsonMockResponse(String bodyJson) {
    return new MockResponse().setHeader("Content-Type", "application/json").setBody(bodyJson);
  }

  protected String buildConflictErrorJson(int statusCode) {
    return String.format(
        "{\"code\": %d, \"message\": \"%s\", \"gid\": \"%s\"}",
        statusCode, JSON_ERROR_MESSAGE, CONFLICT_GID);
  }

  protected String buildErrorJson(int statusCode) {
    return String.format("{\"code\": %d, \"message\": \"%s\"}", statusCode, JSON_ERROR_MESSAGE);
  }

  protected IcApiFacade initJsonResponseMock(BuilderBase modelBuilder) throws Exception {
    String json = modelBuilder.buildJson();
    MockResponse mockResponse = buildBodiedJsonMockResponse(json);
    return initMockServer(mockResponse);
  }

  protected IcApiFacade initMockServer(MockResponse response) throws Exception {
    this.mockServer.addMockResponse(response);

    this.mockServer.start();
    URI baseUri = this.mockServer.getBaseUri();
    return new IcApiFacade(TEST_API_TOKEN, baseUri);
  }

    protected void assertSentCorrectCoreHeaders(
            RecordedRequest request, String endpoint, URI baseUrl, RequestType requestType) {
        MockServerFacade.assertApiEndpointHit(request, endpoint);
        MockServerFacade.assertHeaderContainsValue(request, "Authorization", TEST_API_TOKEN);
        MockServerFacade.assertHeaderContainsValue(request, "Authorization", "Bearer");
        MockServerFacade.assertHeaderContainsValue(request, "Accept", "application/json");
        MockServerFacade.assertHeaderContainsValue(request, "Host", baseUrl.getHost());
        MockServerFacade.assertHasHeader(request, "Date");
        MockServerFacade.assertRequestLineContains(request, requestType.toString());
    }

  private void assertSentCorrectJsonHeaders(
      RecordedRequest request, String endpoint, URI baseUri, RequestType requestType) {
    MockServerFacade.assertApiEndpointHit(request, endpoint);
    this.assertSentCorrectCoreHeaders(request, endpoint, baseUri, requestType);
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "application/json");
    MockServerFacade.assertHeaderContainsValue(request, "Content-Type", "utf-8");
    MockServerFacade.assertHasHeader(request, "Content-Length");
  }

  @AfterEach
  private void closeServer() throws IOException {
    mockServer.close();
  }

  @BeforeEach
  private void startServer() {
    mockServer = new MockServerFacade();
  }
}
