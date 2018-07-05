package com.ic.invoicecapture.connection;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.javatuples.Pair;

public class CloseableRequest implements IRequest {
  private CloseableHttpClient httpClient;
  private HttpUriRequest request;

  public CloseableRequest(CloseableHttpClient httpClient, HttpUriRequest request) {
    this.httpClient = httpClient;
    this.request = request;
  }

  public Pair<StatusLine, HttpEntity> exchangeMessages() throws IOException, ClientProtocolException {
    CloseableHttpResponse response = this.httpClient.execute(this.request);
    
    HttpEntity body = null;
    StatusLine statusLine = null;
    try {
      statusLine = response.getStatusLine();
      body = response.getEntity();

      EntityUtils.consume(body);
    } catch (Exception e) {
      response.close(); //close connection
      throw e;
    } finally {
      response.close();
    }
    
    return Pair.with(statusLine, body);
  }

}
