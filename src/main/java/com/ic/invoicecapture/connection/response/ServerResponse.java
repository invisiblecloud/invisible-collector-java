package com.ic.invoicecapture.connection.response;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;

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

}
