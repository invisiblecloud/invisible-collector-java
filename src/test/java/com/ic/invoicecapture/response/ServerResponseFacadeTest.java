package com.ic.invoicecapture.response;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ic.invoicecapture.StringUtils;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.exceptions.IcException;

public class ServerResponseFacadeTest {

  private static final String TEST_STRING = "test";
  private static final String HEADER_NAME1 = "name1";
  private static final String HEADER_VALUE1 = "value1";
  private static final String HEADER_NAME2 = "name2";
  private static final String HEADER_VALUE2 = "value2";
  private static final String HEADER_VALUE3 = "value3";

  private class HttpResponseBuilder {
    private BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 200, "OK");
    
    public void addHeader(String name, String value) {
      Header header = new BasicHeader(name, value);
      response.addHeader(name, value);
    }
    
    public void setBody(String body) {
      HttpEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
      this.response.setEntity(entity);
    }
    
    public void setBody(HttpEntity entity) {
      this.response.setEntity(entity);
    }
    
    public HttpResponse build() {
      return this.response;
    }
    
    public ServerResponseFacade buildResponseFacade() {
      HttpResponse response = this.build();
      return new ServerResponseFacade(response);
    }
  }
  
  @Test
  public void getBodyEntity_fail() {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    ServerResponseFacade response = builder.buildResponseFacade();
    
    Assertions.assertThrows(IcException.class, response::getBodyAsString);
  }
  
  @Test
  public void getBodyAsString_success() throws UnsupportedEncodingException, IcException {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    ServerResponseFacade response = builder.buildResponseFacade();
   
    String bodyString = response.getBodyAsString();
    Assertions.assertTrue(bodyString.contains(TEST_STRING));
  }

  @Test
  public void getBodyAsString_fail() throws UnsupportedEncodingException, IcException {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody((HttpEntity) null);
    ServerResponseFacade response = builder.buildResponseFacade();
    
    Assertions.assertThrows(IcException.class, response::getBodyAsString);
  }
  
  @Test
  public void getBodyEntityContent_success() throws UnsupportedEncodingException, IcException {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    ServerResponseFacade response = builder.buildResponseFacade();
   
    InputStream inputStream = response.getConnectionStream();
    String bodyString = StringUtils.inputStreamToString(inputStream); 
    Assertions.assertTrue(bodyString.contains(TEST_STRING));
  }
  
  @Test
  public void checkContainsHeaderValue_success() throws UnsupportedEncodingException, IcException {
    HttpResponseBuilder builder = new HttpResponseBuilder();
    builder.setBody(TEST_STRING);
    
    ServerResponseFacade response = builder.buildResponseFacade();
   
    InputStream inputStream = response.getConnectionStream();
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
