package com.ic.invoicecapture.connection.request;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.javatuples.Pair;
import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.exceptions.ICIOxception;

public class CloseableExchanger implements IMessageExchanger {
  private CloseableHttpClient httpClient;
  private HttpUriRequest request;

  public CloseableExchanger(CloseableHttpClient httpClient, HttpUriRequest request) {
    this.httpClient = httpClient;
    this.request = request;
  }

  public ServerResponse exchangeMessages() throws IOException {
    CloseableHttpResponse response = null;
    try {
      response = this.httpClient.execute(this.request);
    } catch (ClientProtocolException e) {
      throw new ICIOxception("HTTP protocol error: " + e.getMessage());
    }

    HttpEntity bodyEntity = null;
    StatusLine statusLine = null;
    try {
      statusLine = response.getStatusLine();
      bodyEntity = response.getEntity();

      EntityUtils.consume(bodyEntity);
    } catch (Exception e) {
      response.close(); // close connection
      throw e;
    } finally {
      response.close();
    }

    return new ServerResponse(statusLine, bodyEntity);
  }

  public static CloseableExchanger buildExchanger(HttpUriRequest request) {
    CloseableHttpClient httpClient = HttpClients.createMinimal();
    return new CloseableExchanger(httpClient, request);
  }
  
}
