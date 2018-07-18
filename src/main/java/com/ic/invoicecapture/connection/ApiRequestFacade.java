package com.ic.invoicecapture.connection;

import com.ic.invoicecapture.connection.request.HttpRequestBuilder;
import com.ic.invoicecapture.connection.request.MessageExchanger;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.InputStream;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ApiRequestFacade {

  private static final String X_API_TOKEN_NAME = "X-Api-Token";
  private static final String CONTENT_TYPE = "application/json";
  private static final String SENT_CONTENT_TYPE = CONTENT_TYPE + "; charset=utf-8";

  private final String apiToken;
  private final URI baseUrl;
  private final MessageExchanger exchanger; // must be thread-safe
  private HttpRequestBuilder requestBuilder;

  public ApiRequestFacade(String apiToken, URI baseUrl) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.requestBuilder = new HttpRequestBuilder();
    this.exchanger = new MessageExchanger();
  }

  public ApiRequestFacade(String apiToken, URI baseUrl, MessageExchanger exchanger,
      HttpRequestBuilder requestBuilder) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.requestBuilder = requestBuilder;
    this.exchanger = exchanger;
  }

  private ApiRequestFacade addCommonHeaders(HttpRequestBuilder requestBuilder) {
    requestBuilder.addHeader(X_API_TOKEN_NAME, this.apiToken);
    String host = this.baseUrl.getHost();
    final int port = this.baseUrl.getPort();
    if (port >= 0) {
      host += ":" + port;
    }
    requestBuilder.addHeader("Host", host);
    requestBuilder.addHeader("Accept", CONTENT_TYPE);
    final String sendDate =
        DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC));
    requestBuilder.addHeader("Date", sendDate);
    return this;
  }

  private ApiRequestFacade addBodyHeaders(HttpRequestBuilder requestBuilder) {
    requestBuilder.addHeader("Content-Type", SENT_CONTENT_TYPE);
    return this;
  }

  private HttpRequestBuilder buildRequestBuilder(String urlEndpoint, RequestType requestType) {
    HttpRequestBuilder requestBuilder = this.requestBuilder.clone();
    requestBuilder.setRequestType(requestType);
    requestBuilder.setUri(this.baseUrl, urlEndpoint);

    return requestBuilder;
  }

  private ServerResponseFacade exchangeAndValidateMessages(IValidator validator,
      HttpRequestBuilder requestBuilder) throws IcException {
    ServerResponseFacade responseFacade = exchanger.exchangeMessages(requestBuilder);
    validator.validateAndTryThrowException(responseFacade);

    return responseFacade;
  }

  private InputStream requestGuts(IValidator validator, String urlEndpoint, RequestType requestType,
      String bodyToSend) throws IcException {
    HttpRequestBuilder requestBuilder = buildRequestBuilder(urlEndpoint, requestType);
    this.addCommonHeaders(requestBuilder);
    if (bodyToSend != null) {
      this.addBodyHeaders(requestBuilder);
      requestBuilder.setBody(bodyToSend);
    }

    return this.exchangeAndValidateMessages(validator, requestBuilder).getResponseBodyStream();
  }

  public InputStream getRequest(IValidator validator, String urlEndpoint) throws IcException {
    return this.requestGuts(validator, urlEndpoint, RequestType.GET, null);
  }

  public InputStream putRequest(IValidator validator, String urlEndpoint, String bodyToSend)
      throws IcException {
    return this.requestGuts(validator, urlEndpoint, RequestType.PUT, bodyToSend);
  }

  public InputStream postRequest(IValidator validator, String urlEndpoint, String bodyToSend)
      throws IcException {
    return this.requestGuts(validator, urlEndpoint, RequestType.POST, bodyToSend);
  }
}
