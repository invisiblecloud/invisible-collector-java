package com.ic.invoicecapture.response;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.entity.StringEntity;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

public class ServerResponseTest {
  
  private StatusLine statusLine;

  @BeforeEach
  public void init() {
    this.statusLine = EasyMock.createNiceMock(StatusLine.class);
  }
  
  @Test
  public void getBodyAsString_string() throws UnsupportedEncodingException, IcException {
    final String TEST_STRING = "test";
    HttpEntity bodyEntity = new StringEntity(TEST_STRING, StandardCharsets.UTF_8);
    ServerResponse response = new ServerResponse(this.statusLine, bodyEntity);
    String bodyString = response.getBodyAsString();
    Assertions.assertTrue(bodyString.contains(TEST_STRING)); 
  }
  
  @Test
  public void getBodyAsString_fail() throws UnsupportedEncodingException, IcException {
    
    ServerResponse response = new ServerResponse(this.statusLine, null);
    Assertions.assertThrows(IcException.class, response::getBodyAsString); 
  }
  
}
