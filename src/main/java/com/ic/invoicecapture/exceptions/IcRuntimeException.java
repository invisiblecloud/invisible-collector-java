package com.ic.invoicecapture.exceptions;

import java.net.URISyntaxException;

public class IcRuntimeException extends RuntimeException {

  public IcRuntimeException(String message) {
    super(message);
  }

  public IcRuntimeException(String message, URISyntaxException exception) {
    super(message, exception);
  }

}
