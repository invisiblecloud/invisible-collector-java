package com.ic.invoicecapture.connection.request;

import com.ic.invoicecapture.connection.RequestType;
import java.net.URI;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;


public class HttpUriRequestBuilder {

  // private String body = null;
  private Map<String, String> headers;
  private RequestType requestType = null;
  private URI url = null;
  
  public HttpUriRequestBuilder() {
    this.headers = new TreeMap<>();
  }
  
  public HttpUriRequestBuilder(Map<String, String> headers) {
    this.headers = headers;
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
        request = new HttpGet(this.url);
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
    } else if (url == null) {
      throw new IllegalArgumentException("No URL set");
    }


    HttpUriRequest request = buildRequest();
    return request;
  }

  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }

  public void setUrl(URI url) {
    this.url = url;
  } 
}
