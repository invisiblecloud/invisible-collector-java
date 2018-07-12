package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.RequestType;
import com.ic.invoicecapture.exceptions.IcRuntimeException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;


public class HttpRequestBuilder implements Cloneable, IRequestBuilder {

  // private String body = null;
  private Map<String, String> headers;
  private RequestType requestType = null;
  private URI uri = null;

  public HttpRequestBuilder() {
    this.headers = new TreeMap<>();
  }

  public HttpRequestBuilder(HttpRequestBuilder other) {
    this.requestType = other.requestType;
    this.uri = other.uri;
    this.headers = new TreeMap<String, String>(other.headers);
  }

  public HttpRequestBuilder(Map<String, String> headers) {
    this.headers = headers;
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  // public void setBody(String body) {
  // this.body = body;
  // }

  public HttpUriRequest build()  {
    if (requestType == null) {
      throw new IllegalArgumentException("No Http Request Type set");
    } else if (uri == null) {
      throw new IllegalArgumentException("No URL set");
    }


    HttpUriRequest request = buildRequest();
    return request;
  }

  private HttpUriRequest buildRequest() {
    HttpUriRequest request = null;

    switch (requestType) {
      case GET:
        request = new HttpGet(this.uri);
        break;
      case POST:
      case PUT:

        throw new UnsupportedOperationException("PUT, POST not implemented yet");

      default:
        return null;
    }

    for (Map.Entry<String, String> entry : headers.entrySet()) {
      request.addHeader(entry.getKey(), entry.getValue());
    }

    return request;
  }

  public HttpRequestBuilder clone() {
    try {
      HttpRequestBuilder clone = (HttpRequestBuilder) super.clone();
      clone.headers = new TreeMap<String, String>(this.headers);
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new IcRuntimeException("OO gone wrong");
    }
  }

  public Map<String, String> getHeaders() {
    return new TreeMap<String, String>(this.headers);
  }
  
  public RequestType getRequestType() {
    return this.requestType;
  }
  
  public URI getUri() {
    return this.uri;
  }
  
  private URI joinUris(URI baseUri, String uriEndpoint) {
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

  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }
  
  public void setUri(URI url) {
    this.uri = url;
  }
  
  public void setUri(URI url, String urlEndpoint) {
    this.uri = this.joinUris(url, urlEndpoint);
  }
}
