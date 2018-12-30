package com.invisiblecollector.connection;

import com.invisiblecollector.connection.request.HttpRequestBuilder;
import com.invisiblecollector.connection.request.MessageExchanger;
import com.invisiblecollector.connection.response.ServerResponseFacade;
import com.invisiblecollector.connection.response.validators.IValidator;
import com.invisiblecollector.exceptions.IcException;
import com.sun.security.ntlm.Server;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ApiRequestFacade {

  private static final String X_API_TOKEN_NAME = "Authorization";
  private static final String X_API_TOKEN_PREFIX = "Bearer ";
  private static final String CONTENT_TYPE = "application/json";
  private static final String SENT_CONTENT_TYPE = CONTENT_TYPE + "; charset=utf-8";

  private final String apiToken;
  private final URI baseUrl;

    // assumed to be thread-safe
  private static final Client client = ClientBuilder.newClient();
  static {
    client.property(ClientProperties.FOLLOW_REDIRECTS, true);
  }

  public ApiRequestFacade(String apiToken, URI baseUrl) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
  }

  public ApiRequestFacade(String apiToken, URI baseUrl, MessageExchanger exchanger,
      HttpRequestBuilder requestBuilder) {
    this.apiToken = apiToken;
    this.baseUrl = baseUrl;
  }

  private ApiRequestFacade addCommonHeaders(Invocation.Builder requestBuilder) {
    requestBuilder.header(X_API_TOKEN_NAME, X_API_TOKEN_PREFIX + this.apiToken);
    String host = this.baseUrl.getHost();
    final int port = this.baseUrl.getPort();
    if (port >= 0) {
      host += ":" + port;
    }
    requestBuilder.header("Host", host);
    requestBuilder.header("Accept", CONTENT_TYPE);
    final String sendDate =
        DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC));
    requestBuilder.header("Date", sendDate);
    return this;
  }

  private Response makeRequest(Invocation.Builder request, RequestType requestType, String bodyToSend) {
    if (requestType == RequestType.GET) {
      return request.get();
    }

    if (bodyToSend == null) {
        bodyToSend = "";
    }

    Entity<String> entity = Entity.entity(bodyToSend, SENT_CONTENT_TYPE);
    if (requestType == RequestType.POST) {
      return request.post(entity);
    } else if (requestType == RequestType.PUT) {
      return request.put(entity);
    } else {
      throw new IllegalStateException("Invalid program logic");
    }
  }

  private InputStream requestGuts(IValidator validator, String urlEndpoint, RequestType requestType, String bodyToSend) throws IcException {
    Invocation.Builder request = client.target(baseUrl)
            .path(urlEndpoint)
            .request(MediaType.APPLICATION_JSON);
    this.addCommonHeaders(request);

    Response response = makeRequest(request, requestType, bodyToSend);

    // validation
    ServerResponseFacade serverResponseFacade = new ServerResponseFacade(response);
    validator.assertValidResponse(serverResponseFacade);

    return response.readEntity(InputStream.class);
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
