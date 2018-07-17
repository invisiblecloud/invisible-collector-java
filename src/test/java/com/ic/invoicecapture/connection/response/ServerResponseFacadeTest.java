package com.ic.invoicecapture.connection.response;

import com.ic.invoicecapture.StringUtils;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServerResponseFacadeTest {

  private static final String TEST_STRING = "test";
  private static final String HEADER_NAME1 = "name1";
  private static final String HEADER_VALUE1 = "value1";
  private static final String HEADER_NAME2 = "name2";
  private static final String HEADER_VALUE2 = "value2";
  private static final String HEADER_VALUE3 = "value3";

  private class CloseableBasicHttpResponse extends BasicHttpResponse
      implements CloseableHttpResponse {

    private boolean isCloseCalled = false;

    public CloseableBasicHttpResponse() {
      super(HttpVersion.HTTP_1_1, 200, "OK");
    }

    @Override
    public void close() throws IOException {
      this.isCloseCalled = true;
    }

    public boolean isCloseCalled() {
      return this.isCloseCalled;
    }

  }

  private class HttpResponseBuilder {
    private CloseableBasicHttpResponse response;

    public HttpResponseBuilder() {
      this.response = new CloseableBasicHttpResponse();
    }

    public HttpResponseBuilder(CloseableBasicHttpResponse response) {
      this.response = response;
    }

    public void addHeader(String name, String value) {
      response.addHeader(name, value);
    }

    public void setBody(String body) {
      HttpEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
      this.response.setEntity(entity);
    }

    public void setBody(HttpEntity entity) {
      this.response.setEntity(entity);
    }

    public CloseableHttpResponse build() {
      return this.response;
    }

    public ServerResponseFacade buildResponseFacade() {
      return new ServerResponseFacade(this.build());
    }
  }

  @Test
  public void consumeConnectionAsString_success() throws IcException {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    ServerResponseFacade response = builder.buildResponseFacade();

    String bodyString = response.consumeConnectionAsString();
    Assertions.assertTrue(bodyString.contains(TEST_STRING));
  }

  @Test
  public void consumeConnectionAsString_fail() throws IcException {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody((HttpEntity) null);
    ServerResponseFacade response = builder.buildResponseFacade();

    Assertions.assertThrows(IcException.class, response::consumeConnectionAsString);
  }

  @Test
  public void consumeConnectionAsString_closesConnection() throws IcException, IOException {
    CloseableBasicHttpResponse httpResponse = new CloseableBasicHttpResponse();
    HttpResponseBuilder builder = new HttpResponseBuilder(httpResponse);
    builder.setBody(TEST_STRING);
    ServerResponseFacade response = builder.buildResponseFacade();

    response.consumeConnectionAsString();
    Assertions.assertTrue(httpResponse.isCloseCalled());

  }

  @Test
  public void getResponseBodyStream_success() throws IcException {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    ServerResponseFacade response = builder.buildResponseFacade();

    InputStream inputStream = response.getResponseBodyStream();
    String bodyString = StringUtils.inputStreamToString(inputStream);
    Assertions.assertTrue(bodyString.contains(TEST_STRING));
  }

  @Test
  public void getResponseBodyStream_closesConnection() throws IcException, IOException {
    CloseableBasicHttpResponse httpResponse = new CloseableBasicHttpResponse();
    HttpResponseBuilder builder = new HttpResponseBuilder(httpResponse);
    builder.setBody(TEST_STRING);
    ServerResponseFacade response = builder.buildResponseFacade();

    response.getResponseBodyStream()
        .close();
    Assertions.assertTrue(httpResponse.isCloseCalled());
  }
  
  @Test
  public void checkContainsHeaderValue_success() throws IcException {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);

    ServerResponseFacade response = builder.buildResponseFacade();

    InputStream inputStream = response.getResponseBodyStream();
    String bodyString = StringUtils.inputStreamToString(inputStream);
    Assertions.assertTrue(bodyString.contains(TEST_STRING));
  }

  @Test
  public void getHeaderValues_oneHeader() {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    builder.addHeader(HEADER_NAME1, HEADER_VALUE1);
    ServerResponseFacade response = builder.buildResponseFacade();
    String headerValue = response.getHeaderValues(HEADER_NAME1);
    Assertions.assertTrue(headerValue.contains(HEADER_VALUE1));
  }

  @Test
  public void getHeaderValues_multipleHeaders() {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    builder.addHeader(HEADER_NAME1, HEADER_VALUE1);
    builder.addHeader(HEADER_NAME2, HEADER_VALUE2);
    ServerResponseFacade response = builder.buildResponseFacade();
    String headerValue = response.getHeaderValues(HEADER_NAME1);
    Assertions.assertTrue(headerValue.contains(HEADER_VALUE1));

    String headerValue2 = response.getHeaderValues(HEADER_NAME2);
    Assertions.assertTrue(headerValue2.contains(HEADER_VALUE2));
  }

  @Test
  public void getHeaderValues_fail() {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    builder.addHeader(HEADER_NAME1, HEADER_VALUE1);
    builder.addHeader(HEADER_NAME2, HEADER_VALUE2);
    ServerResponseFacade response = builder.buildResponseFacade();
    String headerValue = response.getHeaderValues(HEADER_NAME2);
    Assertions.assertFalse(headerValue.contains(HEADER_VALUE1));
  }

  @Test
  public void getHeaderValues_multipleHeadersWithSameName() {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    builder.addHeader(HEADER_NAME1, HEADER_VALUE1);
    builder.addHeader(HEADER_NAME1, HEADER_VALUE2);
    ServerResponseFacade response = builder.buildResponseFacade();
    String headerValue = response.getHeaderValues(HEADER_NAME1);
    Assertions.assertTrue(headerValue.contains(HEADER_VALUE1));
    Assertions.assertTrue(headerValue.contains(HEADER_VALUE2));
  }

  @Test
  public void getHeaderValues_multipleHeaderValues() {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    builder.addHeader(HEADER_NAME1, HEADER_VALUE1);
    builder.addHeader(HEADER_NAME1, HEADER_VALUE2 + "," + HEADER_VALUE3);
    ServerResponseFacade response = builder.buildResponseFacade();
    String headerValue = response.getHeaderValues(HEADER_NAME1);
    Assertions.assertTrue(headerValue.contains(HEADER_VALUE1));
    Assertions.assertTrue(headerValue.contains(HEADER_VALUE2));
    Assertions.assertTrue(headerValue.contains(HEADER_VALUE3));
  }
}
