package com.ic.invoicecapture.connection;

import com.ic.invoicecapture.builders.IBuilder;
import com.ic.invoicecapture.connection.request.ClosingExchanger;
import com.ic.invoicecapture.connection.request.HttpUriRequestBuilder;
import com.ic.invoicecapture.connection.request.IMessageExchanger;
import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorFactory;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.exceptions.IcRuntimeException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpUriRequest;

public class ApiRequestFacade {

  private static final String X_API_TOKEN_NAME = "X-Api-Token";
  private static final String CONTENT_TYPE = "application/json";

  private final String apiToken;
  private final URI baseUrl;
  private final IBuilder<IMessageExchanger, HttpUriRequest> exchangerBuilder; // must be thread-safe
  private HttpUriRequestBuilder requestBuilder;
  private ValidatorFactory validatorFactory;

  public ApiRequestFacade(String apiToken, URI baseUrl) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.requestBuilder = new HttpUriRequestBuilder();
    this.exchangerBuilder = (request) -> ClosingExchanger.buildExchanger(request);
    this.validatorFactory = new ValidatorFactory();

    this.addRequestBuilderHeaders(this.requestBuilder);
  }

  public ApiRequestFacade(String apiToken, URI baseUrl,
      IBuilder<IMessageExchanger, HttpUriRequest> exchangerBuilder,
      HttpUriRequestBuilder requestBuilder, ValidatorFactory validatorFactory) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.requestBuilder = requestBuilder;
    this.exchangerBuilder = exchangerBuilder;
    this.validatorFactory = validatorFactory;

    this.addRequestBuilderHeaders(this.requestBuilder);
  }

  private void addRequestBuilderHeaders(HttpUriRequestBuilder requestBuilder) {
    requestBuilder.addHeader(X_API_TOKEN_NAME, this.apiToken);
    requestBuilder.addHeader("Content-Type", CONTENT_TYPE);
    requestBuilder.addHeader("Accept", CONTENT_TYPE);
  }

  private IMessageExchanger buildExchanger(String urlEndpoint, RequestType requestType,
      HttpUriRequestBuilder requestBuilder) {
    URI url = ApiRequestFacade.joinUris(this.baseUrl, urlEndpoint);

    requestBuilder.setRequestType(requestType);
    requestBuilder.setUri(url);
    HttpUriRequest request = requestBuilder.build();
    return this.exchangerBuilder.build(request);
  }

  public InputStream getRequest(String urlEndpoint)
      throws IOException, IcException {
    HttpUriRequestBuilder requestBuilder = this.requestBuilder.clone();
    IMessageExchanger exchanger = buildExchanger(urlEndpoint, RequestType.GET, requestBuilder);
    ServerResponse responsePair = exchanger.exchangeMessages();

    IValidator validator = this.validatorFactory.build(RequestType.GET, responsePair);
    validator.validateAndTryThrowException(); // can throw exception

    return responsePair.getBodyEntity().getContent();
  }

  public static URI joinUris(URI baseUri, String uriEndpoint) {
    if (uriEndpoint == null || uriEndpoint.equals("")) {
      return baseUri.normalize();
    }
    try {
      URI url = new URI(baseUri.toString() + "/" + uriEndpoint);
      return url.normalize();
    } catch (URISyntaxException exception) {
      throw new IcRuntimeException(exception.getMessage());
    }
  }
}
