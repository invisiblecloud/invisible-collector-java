package com.ic.invoicecapture.connection.request;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.ic.invoicecapture.exceptions.IcException;

public class ClosingExchangerTest {

  private CloseableHttpResponse response;
  private HttpUriRequest request;
  private HttpEntity bodyEntity;
  private IEntityConsumer consumer;
  private CloseableHttpClient client;
  

  public void initMocks() {
    this.request = EasyMock.createNiceMock(HttpUriRequest.class);
    this.response = EasyMock.createNiceMock(CloseableHttpResponse.class);
    this.bodyEntity = EasyMock.createNiceMock(HttpEntity.class);
    StatusLine statusLine = EasyMock.createNiceMock(StatusLine.class);
    EasyMock.expect(response.getStatusLine()).andReturn(statusLine);
    EasyMock.expect(response.getEntity()).andReturn(bodyEntity);

    this.consumer = EasyMock.createNiceMock(IEntityConsumer.class);
    this.client = EasyMock.createNiceMock(CloseableHttpClient.class);

  }

  @Test
  public void exchangeMessages_sucess() throws ClientProtocolException, IOException, IcException {
    this.initMocks();

    this.response.close();
    EasyMock.expectLastCall();
    EasyMock.replay(this.response);

    this.consumer.consume(this.bodyEntity);
    EasyMock.expectLastCall();
    EasyMock.replay(this.consumer);

    EasyMock.expect(this.client.execute(this.request)).andReturn(this.response);
    EasyMock.replay(this.client);

    ClosingExchanger exchanger = new ClosingExchanger(this.client, this.request, this.consumer);
    Assertions.assertNotEquals(null, exchanger.exchangeMessages());

    EasyMock.verify(this.response);
    EasyMock.verify(this.consumer);
    EasyMock.verify(this.client);
  }

  @Test
  public void exchangeMessages_clientExecuteException()
      throws ClientProtocolException, IOException, IcException {

    HttpUriRequest request = EasyMock.createNiceMock(HttpUriRequest.class);

    CloseableHttpClient client = EasyMock.createNiceMock(CloseableHttpClient.class);
    ClientProtocolException clientException = new ClientProtocolException();
    EasyMock.expect(client.execute(request)).andThrow(clientException);
    EasyMock.replay(client);

    ClosingExchanger exchanger = new ClosingExchanger(client, request);
    Assertions.assertThrows(IcException.class, exchanger::exchangeMessages);

    EasyMock.verify(client);
  }

  @Test
  public void exchangeMessages_consumeException()
      throws ClientProtocolException, IOException, IcException {

    this.initMocks();

    this.response.close();
    EasyMock.expectLastCall();
    EasyMock.replay(this.response);

    this.consumer.consume(this.bodyEntity);
    IOException exception = new IOException();
    EasyMock.expectLastCall().andThrow(exception);
    EasyMock.replay(this.consumer);

    EasyMock.expect(this.client.execute(this.request)).andReturn(this.response);
    EasyMock.replay(this.client);

    ClosingExchanger exchanger = new ClosingExchanger(this.client, this.request, this.consumer);
    Assertions.assertThrows(IcException.class, exchanger::exchangeMessages);

    EasyMock.verify(this.consumer);
    EasyMock.verify(this.client);
  }

  @Test
  public void buildExchanger_correctness() {
    this.request = EasyMock.createNiceMock(HttpUriRequest.class);
    Assertions.assertNotEquals(null, ClosingExchanger.buildExchanger(this.request));
  }
  
}
