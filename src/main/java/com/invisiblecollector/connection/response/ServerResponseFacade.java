package com.invisiblecollector.connection.response;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.invisiblecollector.connection.CloseableBufferedInputStream;
import com.invisiblecollector.exceptions.IcException;

import javax.ws.rs.core.Response;

public class ServerResponseFacade implements IServerResponse {

  private Response response;

  public ServerResponseFacade(Response response) {

    this.response = response;
  }

  public ServerResponseFacade(CloseableHttpResponse response) {

  }

  @Override
  public String consumeConnectionAsString() {
    return this.response.readEntity(String.class);
  }

  /**
   * Returns a string with the header values separated by ',' (comma). A single value has no commas
   * 
   * @param headerName name of the HTTP header
   * @return the header value(s)
   */
  @Override
  public String getHeaderValues(String headerName) {
    return this.response.getHeaderString(headerName);
  }

  @Override
  public int getStatusCode() {
    return this.response.getStatus();
  }

  @Override
  public String getStatusCodeReasonPhrase() {
    return this.response.getStatusInfo().getReasonPhrase();
  }



}
