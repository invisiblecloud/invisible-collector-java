package com.ic.invisiblecollector.exceptions;

import java.net.URISyntaxException;

public class IcRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public IcRuntimeException(String message) {
    super(message);
  }

  public IcRuntimeException(String message, URISyntaxException exception) {
    super(message, exception);
  }

}
