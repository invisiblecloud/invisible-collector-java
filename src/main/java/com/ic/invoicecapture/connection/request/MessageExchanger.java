package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class MessageExchanger implements IMessageExchanger {

  private CloseableHttpClient httpClient;
  private HttpUriRequest request;

  public MessageExchanger(CloseableHttpClient httpClient, HttpUriRequest request) {
    this.httpClient = httpClient;
    this.request = request;
  }

  public MessageExchanger(CloseableHttpClient httpClient, HttpUriRequest request,
      IEntityConsumer entityConsumer) {
    this.httpClient = httpClient;
    this.request = request;
  }

  public static MessageExchanger buildExchanger(HttpUriRequest request) {
    CloseableHttpClient httpClient = HttpClients.createMinimal();
    return new MessageExchanger(httpClient, request);
  }

  public ServerResponseFacade exchangeMessages() throws IcException {
    CloseableHttpResponse response = null;
    try {
      response = this.httpClient.execute(this.request);
    } catch (IOException e) {
      throw new IcException("HTTP protocol error", e);
    }

    return new ServerResponseFacade(response);
  }

}
