package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.RequestType;
import com.ic.invoicecapture.exceptions.IcRuntimeException;
import java.net.URI;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;


public class HttpUriRequestBuilder implements Cloneable {

  // private String body = null;
  private Map<String, String> headers;
  private RequestType requestType = null;
  private URI uri = null;

  public HttpUriRequestBuilder() {
    this.headers = new TreeMap<>();
  }

  public HttpUriRequestBuilder(Map<String, String> headers) {
    this.headers = headers;
  }

  public HttpUriRequestBuilder(HttpUriRequestBuilder other) {
    this.requestType = other.requestType;
    this.uri = other.uri;
    this.headers = new TreeMap<String, String>(other.headers);
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  // public void setBody(String body) {
  // this.body = body;
  // }

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

  public HttpUriRequest build() throws IllegalArgumentException {
    if (requestType == null) {
      throw new IllegalArgumentException("No Http Request Type set");
    } else if (uri == null) {
      throw new IllegalArgumentException("No URL set");
    }


    HttpUriRequest request = buildRequest();
    return request;
  }

  public HttpUriRequestBuilder clone() {
    try {
      HttpUriRequestBuilder clone = (HttpUriRequestBuilder) super.clone();
      clone.headers = new TreeMap(this.headers);
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new IcRuntimeException("OO gone wrong");
    }
  }

  public RequestType getRequestType() {
    return this.requestType;
  }
  
  public URI getUri() {
    return this.uri;
  }
  
  public Map<String, String> getHeaders() {
    return new TreeMap<String, String>(this.headers);
  }
  
  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }

  public void setUri(URI url) {
    this.uri = url;
  }
}
