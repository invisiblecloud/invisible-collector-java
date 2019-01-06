package com.invisiblecollector.connection;

import com.invisiblecollector.connection.response.ResponseValidator;
import com.invisiblecollector.exceptions.IcException;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ApiRequestFacade {

  private static final String X_API_TOKEN_NAME = "Authorization";
  private static final String X_API_TOKEN_PREFIX = "Bearer ";
  private static final String JSON_TYPE = "application/json";
  private static final String JSON_WITH_CHARSET_TYPE = JSON_TYPE + "; charset=utf-8";

  private static Client clientInstance = null; // assumed to be thread-safe

  private final String apiToken;
  private final URI baseUrl;
  private final Client client;
  private final ResponseValidator responseValidator;

  public ApiRequestFacade(String apiToken, URI baseUrl, ResponseValidator responseValidator) {
    this(apiToken, baseUrl, responseValidator, getClientInstance());
  }

  public ApiRequestFacade(
      String apiToken, URI baseUrl, ResponseValidator responseValidator, Client client) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
    this.responseValidator = responseValidator;
    this.client = client;
  }

  private static Client getClientInstance() {
    if (clientInstance != null) {
      return clientInstance;
    }

    clientInstance = ClientBuilder.newClient();
    clientInstance.property(ClientProperties.FOLLOW_REDIRECTS, true);
    return clientInstance;
  }

  private ApiRequestFacade addCommonHeaders(Invocation.Builder requestBuilder) {
    requestBuilder.header(X_API_TOKEN_NAME, X_API_TOKEN_PREFIX + this.apiToken);
    String host = this.baseUrl.getHost();
    final int port = this.baseUrl.getPort();
    if (port >= 0) {
      host += ":" + port;
    }
    requestBuilder.header("Host", host);
    requestBuilder.header("Accept", JSON_TYPE);
    final String sendDate =
        DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC));
    requestBuilder.header("Date", sendDate);
    return this;
  }

  private<T> Response makeRequest(
      Invocation.Builder request, RequestType requestType, T bodyToSend, String contentType) {
    if (requestType == RequestType.GET) {
      return request.get();
    }

    Entity entity;
    if (bodyToSend == null) {
      entity = Entity.entity("", contentType);
    } else {
      entity = Entity.entity(bodyToSend, contentType);
    }

    switch (requestType) {
      case PUT:
        return request.put(entity);
      case POST:
        return request.post(entity);
      default:
        throw new IllegalStateException("Invalid program state");
    }
  }

  /**
   * Sends a JSON request expecting JSON in return
   *
   * @param requestType HTTP request type
   * @param urlEndpoint path
   * @param bodyToSend can be null if no body present
   * @return response body
   * @throws IcException
   */
  public<T> InputStream jsonToJsonRequest(
      RequestType requestType, String urlEndpoint, T bodyToSend) throws IcException {
    Invocation.Builder request =
        client.target(baseUrl).path(urlEndpoint).request(MediaType.APPLICATION_JSON);
    this.addCommonHeaders(request);

    Response response = makeRequest(request, requestType, bodyToSend, JSON_WITH_CHARSET_TYPE);

    responseValidator.assertApiJsonResponse(response);

    return response.readEntity(InputStream.class);
  }

  public InputStream uriEncodedToJsonRequest(
      RequestType requestType, String urlEndpoint, Map<String, Object> uriQuery)
      throws IcException {

    WebTarget path = client.target(baseUrl).path(urlEndpoint);
    for (Map.Entry<String, Object> e : uriQuery.entrySet()) {
        path = path.queryParam(e.getKey(), e.getValue());
    }

    Invocation.Builder request = path.request(MediaType.APPLICATION_JSON);
    this.addCommonHeaders(request);

    Response response = makeRequest(request, requestType, null, JSON_WITH_CHARSET_TYPE);

    responseValidator.assertApiJsonResponse(response);

    return response.readEntity(InputStream.class);
  }
}
