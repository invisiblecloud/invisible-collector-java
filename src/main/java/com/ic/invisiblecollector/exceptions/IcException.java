package com.ic.invisiblecollector.exceptions;

public class IcException extends Exception {

  private static final long serialVersionUID = 1L;

  public IcException() {
    super();
  }

  public IcException(String msg) {
    super(msg);
  }

  public IcException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public IcException(Throwable e) {
    super(e);
  }
}
