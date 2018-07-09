package com.ic.invoicecapture.connection.response;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import com.ic.invoicecapture.exceptions.IcException;

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
      return EntityUtils.toString(this.bodyEntity);
    } catch (Exception e) {
      throw new IcException("Failed to parse response body");
    }
    
  }

}
