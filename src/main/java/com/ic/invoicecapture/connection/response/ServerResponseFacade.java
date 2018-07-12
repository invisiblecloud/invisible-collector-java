package com.ic.invoicecapture.connection.response;

import com.ic.invoicecapture.connection.BufferedInputStreamCloseableDecorator;
import com.ic.invoicecapture.exceptions.IcException;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class ServerResponseFacade implements IResponseStatus, IResponseHeaders {

  private boolean isConnectionOpen;
  private CloseableHttpResponse response;

  public ServerResponseFacade(CloseableHttpResponse response) {
    if (response == null) {
      throw new IllegalArgumentException("response cannot be null");
    }

    this.response = response;
    this.isConnectionOpen = true;
  }

  private void close() throws IcException {
    if (this.isConnectionOpen) {
      try {
        EntityUtils.consume(this.getBodyEntity());
        this.response.close();
      } catch (IOException e) {
        throw new IcException(e);
      }
    }
    
    this.isConnectionOpen = false;
  }

  /**
   * Dangerous. This method will consume the connection and close it.
   * @return the response string body
   * @throws IcException in case of connection error or parsing error
   */
  public String consumeConnectionAsString() throws IcException {
    try {
      String bodyString = EntityUtils.toString(this.getBodyEntity(), StandardCharsets.UTF_8);
      this.close();
      return bodyString;
    } catch (IOException e) {
      throw new IcException("Failed to parse response body", e);
    }
  }

  private HttpEntity getBodyEntity() throws IcException {
    HttpEntity entity = response.getEntity();
    if (entity == null) {
      throw new IcException("No entity body");
    }
    return entity;
  }
  
  public InputStream getConnectionStream() throws IcException {
    InputStream is;
    try {
      is = this.getBodyEntity().getContent();
    } catch (IOException e) {
      throw new IcException(e);
    }

    Closeable closeable = () -> {
      try {
        this.close();
      } catch (IcException e) {
        throw new IOException(e);
      }
    };

    return new BufferedInputStreamCloseableDecorator(is, closeable);
  }

  /**
   * Returns a string with the header values separated by ',' (comma). A single value has no commas
   * 
   * @param headerName name of the HTTP header
   * @return the header value(s)
   */
  public String getHeaderValues(String headerName) {
    Header[] headers = this.response.getHeaders(headerName);
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < headers.length; i++) {
      stringBuilder.append(headers[i].getValue());
      if (i < headers.length - 1) {
        stringBuilder.append(",");
      }
    }

    return stringBuilder.toString();
  }

  
  public int getStatusCode() {
    return this.response.getStatusLine().getStatusCode();
  }

  public String getStatusCodeReasonPhrase() {
    return this.response.getStatusLine().getReasonPhrase();
  }
  
  
  
}
