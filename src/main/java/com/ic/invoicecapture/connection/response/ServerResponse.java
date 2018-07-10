package com.ic.invoicecapture.connection.response;

import com.ic.invoicecapture.exceptions.IcException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

public class ServerResponse {

  private StatusLine statusLine;
  private HttpEntity bodyEntity;

  public ServerResponse(StatusLine statusLine, HttpEntity bodyEntity) {
    this.statusLine = statusLine;
    this.bodyEntity = bodyEntity;
  }

  public StatusLine getStatusLine() {
    return this.statusLine;
  }

  public HttpEntity getBodyEntity() {
    return this.bodyEntity;
  }

  public String getBodyAsString() throws IcException {
    try {
      return EntityUtils.toString(this.bodyEntity, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new IcException("Failed to parse response body", e);
    }

  }

  public InputStream getBodyEntityContent() throws IcException {
    try {
      return bodyEntity.getContent();
    } catch (Exception e) {
      throw new IcException(e);
    }
  }

}
