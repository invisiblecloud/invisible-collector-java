package com.ic.invoicecapture.connection.response;

import com.ic.invoicecapture.exceptions.IcException;

public interface IServerResponse {
  int getStatusCode();

  String getStatusCodeReasonPhrase();
  
  String consumeConnectionAsString() throws IcException;
  
  String getHeaderValues(String headerName);
}