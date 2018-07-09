package com.ic.invoicecapture.exceptions;

import java.io.IOException;

public class IcIoException extends IOException {
  public IcIoException(String msg) {
    super(msg);
  }

  public IcIoException() {
    super();
  }

}
