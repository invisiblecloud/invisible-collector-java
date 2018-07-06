package com.ic.invoicecapture.connection;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.javatuples.Pair;
import com.google.gson.JsonObject;
import com.ic.invoicecapture.connection.request.CloseableExchanger;
import com.ic.invoicecapture.connection.request.HttpUriRequestBuilder;
import com.ic.invoicecapture.connection.request.IExchangerBuilder;
import com.ic.invoicecapture.connection.request.IMessageExchanger;
import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.connection.response.validators.StatusCodeValidator;
import com.ic.invoicecapture.exceptions.RequestStatusException;
import com.ic.invoicecapture.json.JsonConversion;

public class ApiRequestFacade {
  
  private static final String X_API_TOKEN_NAME = "X-Api-Token";
  private static final String CONTENT_TYPE = "application/json";
  
  private String xApiToken;
  private String baseUrl;
  private HttpUriRequestBuilder requestBuilder;
  private IExchangerBuilder exchangerBuilder;

  public ApiRequestFacade(String xApiToken, String baseUrl) {
    this.xApiToken = xApiToken;
    this.baseUrl = baseUrl;
    this.requestBuilder = new HttpUriRequestBuilder();
    this.exchangerBuilder = (request) -> CloseableExchanger.buildExchanger(request);
    
    this.initHeaders();
  }

  private void initHeaders() {
    this.requestBuilder.addHeader(X_API_TOKEN_NAME, this.xApiToken);
    this.requestBuilder.addHeader("Content-Type", CONTENT_TYPE);
    this.requestBuilder.addHeader("Accept", CONTENT_TYPE);
  }

  public InputStream getRequest(String urlEndpoint) throws IOException, RequestStatusException {
    IMessageExchanger exchanger = buildExchanger(urlEndpoint, RequestType.GET);
    ServerResponse responsePair = exchanger.exchangeMessages();
    StatusCodeValidator validator = new StatusCodeValidator(responsePair);
    Pair<Boolean, RequestStatusException> validationResult = validator.validate();
    if (!validationResult.getValue0()) {
      throw validationResult.getValue1();
    }

    return responsePair.getBodyEntity().getContent();
  }


  private IMessageExchanger buildExchanger(String urlEndpoint, RequestType requestType) {
    this.requestBuilder.setRequestType(requestType);
    String url = UrlOperations.joinUrls(this.baseUrl, urlEndpoint);
    this.requestBuilder.setUrl(url);
    
    HttpUriRequest request = this.requestBuilder.build();
    return this.exchangerBuilder.build(request);
  }
}
