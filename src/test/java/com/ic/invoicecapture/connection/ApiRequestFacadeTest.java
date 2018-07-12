package com.ic.invoicecapture.connection;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.easymock.EasyMock;
import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.ic.invoicecapture.builders.IBuilder;
import com.ic.invoicecapture.connection.request.HttpRequestBuilder;
import com.ic.invoicecapture.connection.request.IMessageExchanger;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorFactory;
import com.ic.invoicecapture.exceptions.IcException;

public class ApiRequestFacadeTest {

  private final String TEST_API_TOKEN = "abcdef";
  private final String TEST_URL_STRING = "http://test.test";
  private final URI TEST_URL = URI.create(TEST_URL_STRING);
  private final String TEST_ENDPOINT = "endpoint";
  private final String ENTITY_MESSAGE = "message";

  private Pair<IMessageExchanger, IBuilder<IMessageExchanger, HttpUriRequest>> buildMessageExchangerMock()
      throws IOException, IcException {

    IMessageExchanger exchanger = EasyMock.createNiceMock(IMessageExchanger.class);
    ServerResponseFacade serverResponse = EasyMock.createNiceMock(ServerResponseFacade.class);
    EasyMock.expect(exchanger.exchangeMessages()).andReturn(serverResponse);
    EasyMock.replay(exchanger);
    HttpEntity entity = new StringEntity(ENTITY_MESSAGE);
    EasyMock.expect(serverResponse.getConnectionStream()).andReturn(entity.getContent());
    EasyMock.replay(serverResponse);
    IBuilder<IMessageExchanger, HttpUriRequest> exchangerBuilder = (_unused) -> exchanger;

    return Pair.with(exchanger, exchangerBuilder);
  }

  private HttpRequestBuilder buildRequestBuilderMock() {
    HttpRequestBuilder requestBuilder = EasyMock.createNiceMock(HttpRequestBuilder.class);
    EasyMock.expect(requestBuilder.clone()).andReturn(requestBuilder);

    return requestBuilder;
  }

  private Pair<IValidator, ValidatorFactory> buildValidatorFactoryMock() {
    ValidatorFactory validatorFactory = EasyMock.createNiceMock(ValidatorFactory.class);
    IValidator validator = EasyMock.createNiceMock(IValidator.class);
    EasyMock.expect(
        validatorFactory.build(EasyMock.isA(RequestType.class), EasyMock.isA(ServerResponseFacade.class)))
        .andReturn(validator);
    EasyMock.replay(validatorFactory);
    
    return Pair.with(validator, validatorFactory);
  }

  @Test
  public void joinUris_emptyEndpoint() throws URISyntaxException {
    URI result = ApiRequestFacade.joinUris(TEST_URL, "");
    Assertions.assertEquals(TEST_URL.toString(), result.toString());
  }

  @Test
  public void joinUris_nonEmptyEndpoint() throws URISyntaxException {
    URI result = ApiRequestFacade.joinUris(TEST_URL, TEST_ENDPOINT);
    final String RESULTING_URL = TEST_URL_STRING + "/" + TEST_ENDPOINT;
    Assertions.assertEquals(RESULTING_URL, result.toString());
  }

  @Test
  public void joinUris_extraSlashes() throws URISyntaxException {
    final String EXTRA_SLASHES_ENDPOINT = "/" + TEST_ENDPOINT;
    URI result = ApiRequestFacade.joinUris(TEST_URL, EXTRA_SLASHES_ENDPOINT);
    final String RESULTING_URL = TEST_URL_STRING + "/" + TEST_ENDPOINT;
    Assertions.assertEquals(RESULTING_URL, result.toString());
  }

  @Test
  public void addRequestBuilderHeaders_correctness() {
    HttpRequestBuilder requestBuilder = EasyMock.createNiceMock(HttpRequestBuilder.class);
    ValidatorFactory validatorFactory = EasyMock.createNiceMock(ValidatorFactory.class);

    requestBuilder.addHeader(EasyMock.eq("Content-Type"), EasyMock.anyString());
    EasyMock.expectLastCall();
    requestBuilder.addHeader(EasyMock.eq("Accept"), EasyMock.anyString());
    EasyMock.expectLastCall();
    requestBuilder.addHeader(EasyMock.eq("X-Api-Token"), EasyMock.anyString());
    EasyMock.expectLastCall();
    EasyMock.replay(requestBuilder);

    IBuilder<IMessageExchanger, HttpUriRequest> exchangerBuilder = (request) -> null;

    new ApiRequestFacade(TEST_API_TOKEN, TEST_URL, exchangerBuilder, requestBuilder,
        validatorFactory);
    EasyMock.verify(requestBuilder);
  }

  @Test
  public void buildExchanger_get() throws IOException, IcException {
    HttpRequestBuilder requestBuilder = this.buildRequestBuilderMock();
    ValidatorFactory validatorFactory = this.buildValidatorFactoryMock().getValue1();

    URI url = URI.create(TEST_URL_STRING + "/" + TEST_ENDPOINT);
    requestBuilder.setRequestType(RequestType.GET);
    EasyMock.expectLastCall();
    requestBuilder.setUri(url);
    EasyMock.expectLastCall();
    EasyMock.expect(requestBuilder.build()).andReturn(null);
    EasyMock.replay(requestBuilder);

    Pair<IMessageExchanger, IBuilder<IMessageExchanger, HttpUriRequest>> exchangerPair =
        this.buildMessageExchangerMock();

    ApiRequestFacade apiFacade = new ApiRequestFacade(TEST_API_TOKEN, TEST_URL,
        exchangerPair.getValue1(), requestBuilder, validatorFactory);
    Assertions.assertNotEquals(null, apiFacade.getRequest(TEST_ENDPOINT));
    EasyMock.verify(requestBuilder);
  }
  
  @Test
  public void getRequest_get() throws IOException, IcException {
    HttpRequestBuilder requestBuilder = this.buildRequestBuilderMock();
    EasyMock.replay(requestBuilder);
    Pair<IValidator, ValidatorFactory> validationPair = this.buildValidatorFactoryMock();
    ValidatorFactory validatorFactory = validationPair.getValue1();
    IValidator validator = validationPair.getValue0();
    validator.validateAndTryThrowException();
    EasyMock.expectLastCall();
    EasyMock.replay(validator);

    Pair<IMessageExchanger, IBuilder<IMessageExchanger, HttpUriRequest>> exchangerPair =
        this.buildMessageExchangerMock();

    ApiRequestFacade apiFacade = new ApiRequestFacade(TEST_API_TOKEN, TEST_URL,
        exchangerPair.getValue1(), requestBuilder, validatorFactory);
    Assertions.assertNotEquals(null, apiFacade.getRequest(TEST_ENDPOINT));
    EasyMock.verify(exchangerPair.getValue0());
    EasyMock.verify(validatorFactory);
    EasyMock.verify(validator);
  }
  
  
}
