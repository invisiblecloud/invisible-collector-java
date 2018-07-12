package com.ic.invoicecapture;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import org.junit.jupiter.api.Assertions;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class MockServerFacade implements Closeable {

  private MockWebServer server;

  public MockServerFacade() {
    this.server = new MockWebServer();
  }

  public URI getBaseUri() {
    return server.url("").uri();
  }

  public void close() throws IOException {
    this.server.shutdown();
  }

  public void start() throws IOException {
    this.server.start();
  }

  public MockResponse addMockResponse(String headerName, String headervalue, String body) {
    MockResponse mockResponse =
        new MockResponse().setHeader(headerName, headervalue).setBody(body);
    server.enqueue(mockResponse);
    return mockResponse;
  }

  RecordedRequest getRequest() throws InterruptedException {
    return this.server.takeRequest();
  }

  public static void assertApiEndpointHit(RecordedRequest request, String endpoint)
      throws InterruptedException {
    Assertions.assertEquals("/" + endpoint, request.getPath());
  }

  public static void assertRequestLineContains(RecordedRequest request, String containedValue) {
    Assertions.assertTrue(request.getRequestLine().contains(containedValue));
  }

  public static void assertHeaderContainsValue(RecordedRequest request, String headerName,
      String headerValue) {
    Assertions.assertTrue(request.getHeader(headerName).contains(headerValue));
  }
}