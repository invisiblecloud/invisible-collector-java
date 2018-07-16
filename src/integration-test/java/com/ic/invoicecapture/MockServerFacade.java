package com.ic.invoicecapture;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Assertions;

public class MockServerFacade implements Closeable {

  private MockWebServer server;
  private URI connectionUrli = null;

  public MockServerFacade() {
    this.server = new MockWebServer();
  }

  public URI getBaseUri() {
    if (this.connectionUrli == null) {
      this.connectionUrli = server.url("").uri();
    }
    return this.connectionUrli;
  }

  public void close() throws IOException {
    this.server.shutdown();
  }

  public void start() throws IOException {
    this.server.start();
  }

  public MockResponse addMockResponse(String headerName, String headervalue, String body) {
    MockResponse mockResponse = new MockResponse().setHeader(headerName, headervalue).setBody(body);
    server.enqueue(mockResponse);
    return mockResponse;
  }

  public void addMockResponse(MockResponse mockResponse) {
    server.enqueue(mockResponse);
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
    Assertions.assertNotNull(request.getHeader(headerName));
    Assertions.assertTrue(request.getHeader(headerName).contains(headerValue));
  }

  public static void assertHasHeader(RecordedRequest request, String headerName) {
    Assertions.assertNotNull(request.getHeader(headerName));
  }


}
