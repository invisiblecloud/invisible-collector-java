package com.ic.invisiblecollector.connection.request;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import com.ic.invisiblecollector.connection.builders.IBuilder;
import com.ic.invisiblecollector.connection.response.ServerResponseFacade;
import com.ic.invisiblecollector.exceptions.IcException;

public class MessageExchanger {

  private CloseableHttpClient httpClient;
  private IBuilder<ServerResponseFacade, CloseableHttpResponse> serverResponseBuilder;

  public MessageExchanger() {
    // auto redirect
    this.httpClient = HttpClientBuilder
        .create()
        .setRedirectStrategy(new LaxRedirectStrategy())
        .build();
    this.serverResponseBuilder = (response) -> new ServerResponseFacade(response);
  }
  
  public MessageExchanger(CloseableHttpClient httpClient) {
    // auto redirect
    this.httpClient = httpClient;
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
