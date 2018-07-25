package com.ic.invisiblecollector.connection.request;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.ic.invisiblecollector.connection.RequestType;
import com.ic.invisiblecollector.connection.request.HttpRequestBuilder;

public class HttpRequestBuilderTest {

  private static final String TEST_URI_STRING = "http://test.test";
  private static final URI TEST_URI = URI.create(TEST_URI_STRING);
  private static final String TEST_ENDPOINT = "endpoint";
  private static final String HEADER_NAME = "test-header-name";
  private static final String HEADER_NAME2 = "test-header-name2";
  private static final String HEADER_VALUE = "test-header-value";
  private static final String HEADER_VALUE2 = "test-header-value2";

  private void assertCorrectUriFormation(URI baseUri, String endpoint, String expectedUrl) {
    HttpRequestBuilder builder = new HttpRequestBuilder();
    builder.setUri(baseUri, endpoint);
    Assertions.assertEquals(expectedUrl, builder.getUri().toString());
  }

  private void assertCorrectUriFormation(String endpoint, String expectedUrl) {
    this.assertCorrectUriFormation(TEST_URI, endpoint, expectedUrl);
  }

  @Test
  public void setUri_emptyEndpoint() throws URISyntaxException {
    this.assertCorrectUriFormation("", TEST_URI.toString());
  }

  @Test
  public void setUri_nonEmptyEndpoint() throws URISyntaxException {
    final String resultingUrl = TEST_URI_STRING + "/" + TEST_ENDPOINT;
    this.assertCorrectUriFormation(TEST_ENDPOINT.toString(), resultingUrl);
  }

  @Test
  public void setUri_extraSlashes() throws URISyntaxException {
    final String extraSlashesEndpoint = "/" + TEST_ENDPOINT;
    final String resultingUrl = TEST_URI_STRING + "/" + TEST_ENDPOINT;
    this.assertCorrectUriFormation(extraSlashesEndpoint, resultingUrl);
  }

  @Test
  public void setUri_fail() throws URISyntaxException {
    final String badEndpoint = "\\dwa ";
    Assertions.assertThrows(Exception.class, () -> this.assertCorrectUriFormation(badEndpoint, ""));
  }

  @Test
  public void build_requestTypeNull() {
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    requestBuilder.setUri(TEST_URI);
    Assertions.assertThrows(IllegalArgumentException.class, requestBuilder::build);
  }

  @Test
  public void build_requestUrlNull() {
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    requestBuilder.setRequestType(RequestType.GET);
    Assertions.assertThrows(IllegalArgumentException.class, requestBuilder::build);
  }

  @Test
  public void build_get() {
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    requestBuilder.setRequestType(RequestType.GET);
    requestBuilder.setUri(TEST_URI);
    HttpUriRequest request = requestBuilder.build();
    Assertions.assertNotEquals(null, request);
    Assertions.assertTrue(request instanceof HttpGet);
  }

  @Test
  public void build_noHeaders() {
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    requestBuilder.setRequestType(RequestType.GET);
    requestBuilder.setUri(TEST_URI);

    HttpUriRequest request = requestBuilder.build();
    Assertions.assertNotEquals(request, null);
    Header[] headers = request.getAllHeaders();
    Assertions.assertEquals(0, headers.length);
  }

  @Test
  public void build_oneHeader() {
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    requestBuilder.setRequestType(RequestType.GET);
    requestBuilder.setUri(TEST_URI);
    requestBuilder.addHeader(HEADER_NAME, HEADER_VALUE);

    HttpUriRequest request = requestBuilder.build();
    Assertions.assertNotEquals(request, null);
    Header[] headers = request.getHeaders(HEADER_NAME);
    Assertions.assertEquals(1, headers.length);
    Assertions.assertEquals(HEADER_VALUE, headers[0].getValue());
  }

  @Test
  public void build_headers() {
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    requestBuilder.setRequestType(RequestType.GET);
    requestBuilder.setUri(TEST_URI);
    requestBuilder.addHeader(HEADER_NAME, HEADER_VALUE);
    requestBuilder.addHeader(HEADER_NAME2, HEADER_VALUE2);

    HttpUriRequest request = requestBuilder.build();
    Assertions.assertNotEquals(request, null);
    Header[] headers = request.getHeaders(HEADER_NAME);
    Assertions.assertEquals(1, headers.length);
    Assertions.assertEquals(HEADER_VALUE, headers[0].getValue());

    Header[] headers2 = request.getHeaders(HEADER_NAME2);
    Assertions.assertEquals(1, headers2.length);
    Assertions.assertEquals(HEADER_VALUE2, headers2[0].getValue());
  }

  @Test
  public void build_headersDuplicateKey() {
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    requestBuilder.setRequestType(RequestType.GET);
    requestBuilder.setUri(TEST_URI);
    requestBuilder.addHeader(HEADER_NAME, HEADER_VALUE);
    requestBuilder.addHeader(HEADER_NAME, HEADER_VALUE2);

    HttpUriRequest request = requestBuilder.build();
    Assertions.assertNotEquals(request, null);
    Header[] headers = request.getHeaders(HEADER_NAME);
    Assertions.assertEquals(1, headers.length);
    Assertions.assertEquals(HEADER_VALUE2, headers[0].getValue());
  }

  @Test
  public void clone_equalCopy() {
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    requestBuilder.setRequestType(RequestType.GET);
    requestBuilder.setUri(TEST_URI);
    requestBuilder.addHeader(HEADER_NAME, HEADER_VALUE);
    requestBuilder.addHeader(HEADER_NAME, HEADER_VALUE2);
    HttpRequestBuilder clone = requestBuilder.clone();

    Assertions.assertEquals(requestBuilder.getUri(), clone.getUri());
    Assertions.assertEquals(requestBuilder.getRequestType(), clone.getRequestType());
    Assertions.assertEquals(requestBuilder.getHeaders(), clone.getHeaders());
  }

}
