package com.ic.invoicecapture.connection;

import com.ic.invoicecapture.connection.request.HttpUriRequestBuilder;
import com.ic.invoicecapture.connection.request.MessageExchanger;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorFactory;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.exceptions.IcRuntimeException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpUriRequest;

public class ApiRequestFacade {

  private static final String X_API_TOKEN_NAME = "X-Api-Token";
  private static final String CONTENT_TYPE = "application/json";

  private final String apiToken;
  private final URI baseUrl;
  private final MessageExchanger exchanger; // must be thread-safe
  private HttpUriRequestBuilder requestBuilder;
  private ValidatorFactory validatorFactory;

  public ApiRequestFacade(String apiToken, URI baseUrl) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.requestBuilder = new HttpUriRequestBuilder();
    this.exchanger = new MessageExchanger();
    this.validatorFactory = new ValidatorFactory();

    this.addRequestBuilderHeaders(this.requestBuilder);
  }

  public ApiRequestFacade(String apiToken, URI baseUrl,
      MessageExchanger exchangerBuilder,
      HttpUriRequestBuilder requestBuilder, ValidatorFactory validatorFactory) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.requestBuilder = requestBuilder;
    this.exchanger = exchangerBuilder;
    this.validatorFactory = validatorFactory;

    this.addRequestBuilderHeaders(this.requestBuilder);
  }

  private void addRequestBuilderHeaders(HttpUriRequestBuilder requestBuilder) {
    requestBuilder.addHeader(X_API_TOKEN_NAME, this.apiToken);
    requestBuilder.addHeader("Content-Type", CONTENT_TYPE);
    requestBuilder.addHeader("Accept", CONTENT_TYPE);
  }

  private HttpUriRequest buildUriRequest(String urlEndpoint, RequestType requestType) {
    URI url = ApiRequestFacade.joinUris(this.baseUrl, urlEndpoint);

    HttpUriRequestBuilder requestBuilder = this.requestBuilder.clone();
    requestBuilder.setRequestType(requestType);
    requestBuilder.setUri(url);
    HttpUriRequest request = requestBuilder.build();
    return request;
  }

  public InputStream getRequest(String urlEndpoint)
      throws IcException {
    
    HttpUriRequest request = buildUriRequest(urlEndpoint, RequestType.GET);
    ServerResponseFacade responsePair = exchanger.exchangeMessages(request);

    IValidator validator = this.validatorFactory.build(RequestType.GET, responsePair);
    validator.validateAndTryThrowException(); // can throw exception

    return responsePair.getConnectionStream();
  }

  public static URI joinUris(URI baseUri, String uriEndpoint) {
    if (uriEndpoint == null || uriEndpoint.equals("")) {
      return baseUri.normalize();
    }
    try {
      URI url = new URI(baseUri.toString() + "/" + uriEndpoint);
      return url.normalize();
    } catch (URISyntaxException exception) {
      throw new IcRuntimeException(exception.getMessage(), exception);
    }
  }
}
