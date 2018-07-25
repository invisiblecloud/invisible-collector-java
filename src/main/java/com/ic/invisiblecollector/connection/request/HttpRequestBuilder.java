package com.ic.invisiblecollector.connection.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import com.ic.invisiblecollector.connection.RequestType;
import com.ic.invisiblecollector.exceptions.IcRuntimeException;


public class HttpRequestBuilder implements Cloneable, IRequestBuilder {

  private String body = null;
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

  public HttpRequestBuilder addHeader(String key, String value) {
    headers.put(key, value);
    return this;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public HttpUriRequest build() {
    if (requestType == null) {
      throw new IllegalArgumentException("No Http Request Type set");
    } else if (uri == null) {
      throw new IllegalArgumentException("No URL set");
    }

    return buildRequest();
  }

  private HttpUriRequest addBodyToRequest(HttpEntityEnclosingRequestBase request) {
    
    if (this.body != null) {
      StringEntity stringEntity = new StringEntity(this.body, StandardCharsets.UTF_8); 
      request.setEntity(stringEntity);
    }
    
    return request;
  }
  
  private HttpUriRequest buildRequest() {
    HttpUriRequest request = null;

    switch (requestType) {
      case GET:
        request = new HttpGet(this.uri);
        break;
      case PUT:
        request = this.addBodyToRequest(new HttpPut(this.uri));
        break;
      case POST:
        request = this.addBodyToRequest(new HttpPost(this.uri));
        break;     
      default:
        throw new IllegalArgumentException("Invalid type " + requestType);
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

  private static URI joinUris(URI baseUri, String uriEndpoint) {
    if (uriEndpoint == null || uriEndpoint.equals("")) {
      return baseUri.normalize();
    }
    try {
      URI url = new URI(baseUri.toString() + "/" + uriEndpoint);
      return url.normalize();
    } catch (URISyntaxException exception) {
      throw new IllegalArgumentException("Invalid url", exception);
    }
  }

  public HttpRequestBuilder setRequestType(RequestType requestType) {
    this.requestType = requestType;
    return this;
  }

  public HttpRequestBuilder setUri(URI url) {
    this.uri = url;
    return this;
  }

  public HttpRequestBuilder setUri(URI url, String urlEndpoint) {
    this.uri = joinUris(url, urlEndpoint);
    return this;
  }
}
