package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.builders.IBuilder;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageExchangerTest {

  private class ServerResponseBuilder
      implements IBuilder<ServerResponseFacade, CloseableHttpResponse> {
    private CloseableHttpResponse response = null;
    private ServerResponseFacade serverResponse;

    public ServerResponseBuilder setServerResponseFacade(ServerResponseFacade serverResponse) {
      this.serverResponse = serverResponse;
      return this;
    }

    @Override
    public ServerResponseFacade build(CloseableHttpResponse response) {
      this.response = response;
      return this.serverResponse;

    }

    public CloseableHttpResponse getResponse() {
      return this.response;
    }
  }
  
  private IRequestBuilder buildRequestBuilderMock(URI baseUrl) {
    HttpUriRequest request = new HttpGet(baseUrl);
    IRequestBuilder builder = EasyMock.createNiceMock(IRequestBuilder.class);
    EasyMock.expect(builder.build()).andReturn(request);
    EasyMock.replay(builder);
    
    return builder;
  }

  @Test
  public void exchangeMessages_success() throws ClientProtocolException, IOException, IcException {
    final String TEST_STRING = "TEST BODY";

    CloseableHttpClient httpClient = HttpClients.createMinimal();
    
    ServerResponseFacade responseFacade = EasyMock.createNiceMock(ServerResponseFacade.class);
    ServerResponseBuilder responseBuilder = new ServerResponseBuilder()
        .setServerResponseFacade(responseFacade);

    MessageExchanger exchanger = new MessageExchanger(httpClient, responseBuilder);

    MockWebServer server = new MockWebServer();
    MockResponse mockResponse = new MockResponse()
        .setHeader("Content-Type", "text/plain")
        .setBody(TEST_STRING);
    server.enqueue(mockResponse);
    server.start();

    URI baseUrl = server.url("").uri();
    IRequestBuilder builder = this.buildRequestBuilderMock(baseUrl);
    exchanger.exchangeMessages(builder);
    HttpEntity entity = responseBuilder.getResponse().getEntity();
    String response = EntityUtils.toString(entity, StandardCharsets.UTF_8);

    Assertions.assertTrue(response.contains(TEST_STRING));

    server.shutdown();
  }
  
  @Test
  public void exchangeMessages_fail() throws ClientProtocolException, IOException, IcException {
    CloseableHttpClient httpClient = EasyMock.createNiceMock(CloseableHttpClient.class);
    IOException exception = new IOException("message");
    EasyMock.expect(httpClient.execute(EasyMock.anyObject())).andThrow(exception);
    EasyMock.replay(httpClient);
    
    ServerResponseFacade responseFacade = EasyMock.createNiceMock(ServerResponseFacade.class);
    ServerResponseBuilder responseBuilder = new ServerResponseBuilder()
        .setServerResponseFacade(responseFacade);

    MessageExchanger exchanger = new MessageExchanger(httpClient, responseBuilder);
    
    URI baseUrl = URI.create("http://test.test");
    IRequestBuilder builder = this.buildRequestBuilderMock(baseUrl);
    Assertions.assertThrows(IcException.class, () -> exchanger.exchangeMessages(builder));
  }

}
