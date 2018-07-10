package com.ic.invoicecapture.exceptions;

public class IcException extends Exception {

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
