package com.ic.invoicecapture.connection;

import java.util.Map;
import java.util.TreeMap;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RequestBuilder {

  public enum RequestTypes {
    GET, POST, PUT
  }

  private String body = null;
  private Map<String, String> headers = new TreeMap<>();
  private RequestTypes requestType = null;
  private String url = null;

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setRequestType(RequestTypes requestType) {
    this.requestType = requestType;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  private HttpUriRequest buildRequest() {
    HttpUriRequest request = null;

    switch (requestType) {
      case GET:
        request = new HttpGet(this.url);
        break;
      default:
        return null;
    }

    for (Map.Entry<String, String> entry : headers.entrySet()) {
      request.addHeader(entry.getKey(), entry.getValue());
    }

    return request;
  }

  public IRequest build() throws IllegalArgumentException {
    if (requestType == null) {
      throw new IllegalArgumentException("No Http Request Type set");
    } else if (url == null) {
      throw new IllegalArgumentException("No URL set");
    }
    
    CloseableHttpClient httpClient = HttpClients.createMinimal();
    HttpUriRequest request = buildRequest();

    return new CloseableRequest(httpClient, request);
  }
}
