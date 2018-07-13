package com.ic.invoicecapture.connection;

import com.ic.invoicecapture.connection.request.HttpRequestBuilder;
import com.ic.invoicecapture.connection.request.MessageExchanger;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.connection.response.validators.ValidatorFactory;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.InputStream;
import java.net.URI;

public class ApiRequestFacade {

  private static final String X_API_TOKEN_NAME = "X-Api-Token";
  private static final String CONTENT_TYPE = "application/json";
  private static final String SENT_CONTENT_TYPE = CONTENT_TYPE + "; charset=utf-8";

  private final String apiToken;
  private final URI baseUrl;
  private final MessageExchanger exchanger; // must be thread-safe
  private HttpRequestBuilder requestBuilder;
  private ValidatorFactory validatorFactory;

  public ApiRequestFacade(String apiToken, URI baseUrl) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.requestBuilder = new HttpRequestBuilder();
    this.exchanger = new MessageExchanger();
    this.validatorFactory = new ValidatorFactory();

    this.addRequestBuilderHeaders(this.requestBuilder);
  }

  public ApiRequestFacade(String apiToken, URI baseUrl,
      MessageExchanger exchanger,
      HttpRequestBuilder requestBuilder, ValidatorFactory validatorFactory) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.requestBuilder = requestBuilder;
    this.exchanger = exchanger;
    this.validatorFactory = validatorFactory;

    this.addRequestBuilderHeaders(this.requestBuilder);
  }

  private void addRequestBuilderHeaders(HttpRequestBuilder requestBuilder) {
    requestBuilder.addHeader(X_API_TOKEN_NAME, this.apiToken);
    requestBuilder.addHeader("Content-Type", SENT_CONTENT_TYPE);
    requestBuilder.addHeader("Accept", CONTENT_TYPE);
  }

  private HttpRequestBuilder buildRequestBuilder(String urlEndpoint, RequestType requestType) {
    HttpRequestBuilder requestBuilder = this.requestBuilder.clone();
    requestBuilder.setRequestType(requestType);
    requestBuilder.setUri(this.baseUrl, urlEndpoint);
    
    return requestBuilder;
  }

  public InputStream getRequest(String urlEndpoint)
      throws IcException {
    
    HttpRequestBuilder request = buildRequestBuilder(urlEndpoint, RequestType.GET);
    ServerResponseFacade responseFacade = exchanger.exchangeMessages(request);

    this.validatorFactory.build(RequestType.GET, responseFacade)
        .validateAndTryThrowException(); // can throw exception

    return responseFacade.getResponseBodyStream();
  }
  
}
