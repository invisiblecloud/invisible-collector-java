package com.ic.invisiblecollector.connection.response;

import com.ic.invisiblecollector.exceptions.IcException;

public interface IServerResponse {
  int getStatusCode();

  String getStatusCodeReasonPhrase();
  
  String consumeConnectionAsString() throws IcException;
  
  String getHeaderValues(String headerName);
}
