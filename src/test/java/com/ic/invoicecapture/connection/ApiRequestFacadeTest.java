package com.ic.invoicecapture.connection;

import com.ic.invoicecapture.StringTestUtils;
import com.ic.invoicecapture.connection.request.HttpRequestBuilder;
import com.ic.invoicecapture.connection.request.IRequestBuilder;
import com.ic.invoicecapture.connection.request.MessageExchanger;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApiRequestFacadeTest {

  private static final String TEST_API_TOKEN = "abcdef";
  private static final String TEST_URL_STRING = "http://test.test";
  private static final URI TEST_URI = URI.create(TEST_URL_STRING);
  private static final String TEST_ENDPOINT = "endpoint";
  private static final String TEST_MESSAGE = "message";
  private static final String TEST_JSON = "{ }";
  
  private IValidator validator;
  private HttpRequestBuilder requestBuilder;
  private MessageExchanger exchanger;
  private ServerResponseFacade serverResponse;

  @BeforeEach
  private void buildMessageExchangerMock() throws IOException, IcException {

    this.exchanger = EasyMock.createNiceMock(MessageExchanger.class);
    this.serverResponse = EasyMock.createNiceMock(ServerResponseFacade.class);
    EasyMock.expect(exchanger.exchangeMessages(EasyMock.isA(IRequestBuilder.class)))
        .andReturn(serverResponse);
    EasyMock.replay(exchanger);

    HttpEntity entity = new StringEntity(TEST_MESSAGE);
    EasyMock.expect(serverResponse.getResponseBodyStream()).andReturn(entity.getContent());
    EasyMock.expect(serverResponse.consumeConnectionAsString()).andReturn(TEST_MESSAGE);
    EasyMock.replay(serverResponse);
  }

  @BeforeEach
  private void buildRequestBuilderMock() {
    this.requestBuilder = EasyMock.createNiceMock(HttpRequestBuilder.class);
    EasyMock.expect(requestBuilder.clone()).andReturn(requestBuilder);
  }

  @BeforeEach
  private void buildValidationMocks() {
    
    this.validator = EasyMock.createNiceMock(IValidator.class);
    this.serverResponse = EasyMock.createMock(ServerResponseFacade.class);
  }

  @Test
  public void getRequest_requestBuilder() throws IcException {
    EasyMock.expect(requestBuilder.addHeader(EasyMock.eq("Accept"), EasyMock.anyString()))
        .andReturn(requestBuilder);
    EasyMock.expect(requestBuilder.addHeader(EasyMock.eq("Authorization"), EasyMock.anyString()))
        .andReturn(requestBuilder);
    EasyMock.expect(requestBuilder.setRequestType(RequestType.GET)).andReturn(requestBuilder);
    EasyMock.expect(requestBuilder.setUri(TEST_URI, TEST_ENDPOINT)).andReturn(requestBuilder);
    EasyMock.replay(requestBuilder);

    new ApiRequestFacade(TEST_API_TOKEN, TEST_URI, exchanger, requestBuilder)
        .getRequest(this.validator, TEST_ENDPOINT);
    EasyMock.verify(requestBuilder);
  }

  @Test
  public void getRequest_validationSuccess() throws IOException, IcException {
    EasyMock.replay(requestBuilder);

    new ApiRequestFacade(TEST_API_TOKEN, TEST_URI, exchanger, requestBuilder)
        .getRequest(this.validator, TEST_ENDPOINT);
  }

  @Test
  public void getRequest_validationFail() throws IOException, IcException {
    EasyMock.replay(requestBuilder);

    IcException exception = new IcException();
    this.validator.validateAndTryThrowException(this.serverResponse);
    EasyMock.expectLastCall().andThrow(exception).andVoid();
    EasyMock.replay(this.validator);

    ApiRequestFacade api =
        new ApiRequestFacade(TEST_API_TOKEN, TEST_URI, exchanger, requestBuilder);
    Assertions.assertThrows(IcException.class, () -> api.getRequest(this.validator, TEST_ENDPOINT));
  }

  @Test
  public void getRequest_correctMessagePiping() throws IOException, IcException {
    EasyMock.replay(requestBuilder);

    InputStream inputStream =
        new ApiRequestFacade(TEST_API_TOKEN, TEST_URI, exchanger, requestBuilder)
            .getRequest(this.validator, TEST_ENDPOINT);

    String receivedMessage = StringTestUtils.inputStreamToString(inputStream);
    Assertions.assertTrue(receivedMessage.contains(TEST_MESSAGE));
  }
  
  @Test
  public void putRequest_correctMessagePiping() throws IOException, IcException {
    EasyMock.replay(requestBuilder);

    InputStream inputStream =
        new ApiRequestFacade(TEST_API_TOKEN, TEST_URI, exchanger, requestBuilder)
            .putRequest(this.validator, TEST_ENDPOINT, TEST_JSON);

    String receivedMessage = StringTestUtils.inputStreamToString(inputStream);
    Assertions.assertTrue(receivedMessage.contains(TEST_MESSAGE));
  }
}
