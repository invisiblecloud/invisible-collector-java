package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ClosingExchanger implements IMessageExchanger {

  private CloseableHttpClient httpClient;
  private HttpUriRequest request;
  private IEntityConsumer entityConsumer;

  public ClosingExchanger(CloseableHttpClient httpClient, HttpUriRequest request) {
    this.httpClient = httpClient;
    this.request = request;
    this.entityConsumer = entity -> EntityUtils.consume(entity);
  }

  public ClosingExchanger(CloseableHttpClient httpClient, HttpUriRequest request,
      IEntityConsumer entityConsumer) {
    this.httpClient = httpClient;
    this.request = request;
    this.entityConsumer = entityConsumer;
  }
  
  public static ClosingExchanger buildExchanger(HttpUriRequest request) {
    CloseableHttpClient httpClient = HttpClients.createMinimal();
    return new ClosingExchanger(httpClient, request);
  }

  public ServerResponse exchangeMessages() throws IOException, IcException {
    CloseableHttpResponse response = null;
    try {
      response = this.httpClient.execute(this.request);
    } catch (ClientProtocolException e) {
      throw new IcException("HTTP protocol error: " + e.getMessage());
    }

    HttpEntity bodyEntity = null;
    StatusLine statusLine = null;
    try {
      statusLine = response.getStatusLine();
      bodyEntity = response.getEntity();

      this.entityConsumer.consume(bodyEntity);
    } catch (IOException e) {
      response.close(); // close connection
      throw e;
    } finally {
      response.close();
    }

    return new ServerResponse(statusLine, bodyEntity);
  }

}
