package com.ic.invoicecapture.connection.response;

import com.ic.invoicecapture.exceptions.IcException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

public class ServerResponseFacade {

  private HttpResponse response;

  public ServerResponseFacade(HttpResponse response) {
    if (response == null) {
      throw new IllegalArgumentException("response cannot be null");
    }

    this.response = response;
  }

  public StatusLine getStatusLine() {
    return this.response.getStatusLine();
  }

  public HttpEntity getBodyEntity() throws IcException {
    HttpEntity entity = response.getEntity();
    if (entity == null) {
      throw new IcException("No entity body");
    }
    return entity;
  }

  public String getBodyAsString() throws IcException {
    try {
      return EntityUtils.toString(this.getBodyEntity(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IcException("Failed to parse response body", e);
    }
  }

  /**
   * Returns a string with the header values separated by ',' (comma). A single value has no commas
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

  public InputStream getBodyEntityContent() throws IcException {
    try {
      return this.getBodyEntity().getContent();
    } catch (IOException e) {
      throw new IcException(e);
    }
  }

}
