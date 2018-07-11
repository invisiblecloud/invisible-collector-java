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

public class MessageExchanger {

  private CloseableHttpClient httpClient;

  public MessageExchanger() {
    this.httpClient = HttpClients.createMinimal();
  }

  public ServerResponseFacade exchangeMessages(HttpUriRequest request) throws IcException {
    CloseableHttpResponse response = null;
    try {
      response = this.httpClient.execute(request);
    } catch (IOException e) {
      throw new IcException("HTTP protocol error", e);
    }

    return new ServerResponseFacade(response);
  }

}
