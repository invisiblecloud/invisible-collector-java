package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.builders.IBuilder;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class MessageExchanger {

  private CloseableHttpClient httpClient;
  private IBuilder<ServerResponseFacade, CloseableHttpResponse> serverResponseBuilder;

  public MessageExchanger() {
    this.httpClient = HttpClients.createMinimal();
    this.serverResponseBuilder = (response) -> new ServerResponseFacade(response);
  }

  public MessageExchanger(CloseableHttpClient httpClient,
      IBuilder<ServerResponseFacade, CloseableHttpResponse> serverResponseBuilder) {
    this.httpClient = httpClient;
    this.serverResponseBuilder = serverResponseBuilder;
  }

  public ServerResponseFacade exchangeMessages(IRequestBuilder requestBuilder) throws IcException {
    CloseableHttpResponse response = null;
    HttpUriRequest request = requestBuilder.build();
    try {
      response = this.httpClient.execute(request);
    } catch (IOException e) {
      throw new IcException("HTTP protocol error", e);
    }

    return this.serverResponseBuilder.build(response);
  }
}
