package com.ic.invoicecapture.connection;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class CloseableRequest implements IRequest {
  private CloseableHttpClient httpClient;
  private HttpUriRequest request;

  public CloseableRequest(CloseableHttpClient httpClient, HttpUriRequest request) {
    this.httpClient = httpClient;
    this.request = request;
  }

  public String sendRequest() throws ParseException, IOException {
    CloseableHttpResponse response = this.httpClient.execute(this.request);
    String body = null;
    Pair<String, String> pair ;
    try {
      System.out.println(response.getStatusLine());
      HttpEntity entity = response.getEntity();

      body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
    } catch (Exception e) {
      response.close(); //close connection
      throw e;
    } finally {
      response.close();
    }
    
    return body;
  }

}
