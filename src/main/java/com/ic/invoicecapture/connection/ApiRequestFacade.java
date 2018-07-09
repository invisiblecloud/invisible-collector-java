package com.ic.invoicecapture.connection;

import com.ic.invoicecapture.connection.request.ClosingExchanger;
import com.ic.invoicecapture.connection.request.HttpUriRequestBuilder;
import com.ic.invoicecapture.connection.request.IExchangerBuilder;
import com.ic.invoicecapture.connection.request.IMessageExchanger;
import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.connection.response.validators.StatusCodeValidator;
import com.ic.invoicecapture.connection.response.validators.ValidationResult;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpUriRequest;
import org.javatuples.Pair;

public class ApiRequestFacade {

  private static final String X_API_TOKEN_NAME = "X-Api-Token";
  private static final String CONTENT_TYPE = "application/json";

  private final String apiToken;
  private final URI baseUrl;
  private final IExchangerBuilder exchangerBuilder; // must be thread-safe

  public ApiRequestFacade(String apiToken, URI baseUrl) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.exchangerBuilder = (request) -> ClosingExchanger.buildExchanger(request);
  }

  private IMessageExchanger buildExchanger(String urlEndpoint, RequestType requestType) throws URISyntaxException {
    HttpUriRequestBuilder requestBuilder = this.buildRequestBuilder();
    requestBuilder.setRequestType(requestType);
    URI url = joinUris(this.baseUrl, urlEndpoint);
    requestBuilder.setUrl(url);

    HttpUriRequest request = requestBuilder.build();
    return this.exchangerBuilder.build(request);
  }
  
  private HttpUriRequestBuilder buildRequestBuilder() {
    HttpUriRequestBuilder requestBuilder = new HttpUriRequestBuilder();

    requestBuilder.addHeader(X_API_TOKEN_NAME, this.apiToken);
    requestBuilder.addHeader("Content-Type", CONTENT_TYPE);
    requestBuilder.addHeader("Accept", CONTENT_TYPE);

    return requestBuilder;
  }

  public InputStream getRequest(String urlEndpoint) throws IOException, URISyntaxException, IcException {
    IMessageExchanger exchanger = buildExchanger(urlEndpoint, RequestType.GET);
    ServerResponse responsePair = exchanger.exchangeMessages();
    
    StatusCodeValidator validator = new StatusCodeValidator(responsePair);
    ValidationResult validationResult = validator.validate();
    validationResult.tryThrowException(); //can throw exception

    return responsePair.getBodyEntity().getContent();
  }
  
  private URI joinUris(URI baseUri, String uriEndpoint) throws URISyntaxException {
    URI url = new URI(baseUri.toString() + "/" + uriEndpoint);
    return url.normalize();
  }

  // TODO add try throw method



}
