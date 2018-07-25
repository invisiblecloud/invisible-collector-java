package com.ic.invisiblecollector.exceptions;

import java.net.URISyntaxException;

/**
 * Used to wrap around other 'fatal' exceptions from 3rd party libraries.
 * 
 * @author ros
 */
public class IcRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public IcRuntimeException(String message) {
    super(message);
  }

  public IcRuntimeException(String message, URISyntaxException exception) {
    super(message, exception);
  }

}
