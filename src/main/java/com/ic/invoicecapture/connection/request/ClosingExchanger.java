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

  public ServerResponseFacade exchangeMessages() throws IcException {
    CloseableHttpResponse response = null;
    try {
      response = this.httpClient.execute(this.request);
    } catch (Exception e) {
      throw new IcException("HTTP protocol error", e);
    }

    try {
      HttpEntity bodyEntity = response.getEntity();
      this.entityConsumer.consume(bodyEntity);
    } catch (IOException e) {
      throw new IcException(e);
    }

    try {
      response.close();
    } catch (IOException e) {
      throw new IcException(e);
    }
    return new ServerResponseFacade(response);
  }

}
