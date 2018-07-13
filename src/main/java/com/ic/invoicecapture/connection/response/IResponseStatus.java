package com.ic.invoicecapture.connection.response;

import com.ic.invoicecapture.exceptions.IcException;

public interface IResponseStatus {
  int getStatusCode();

  String getStatusCodeReasonPhrase();
  
  String consumeConnectionAsString() throws IcException;
}
