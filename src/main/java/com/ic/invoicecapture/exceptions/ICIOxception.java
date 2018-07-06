package com.ic.invoicecapture.exceptions;

import java.io.IOException;

public class ICIOxception extends IOException {
  public ICIOxception(String msg) {
    super(msg);
  }

  public ICIOxception() {
    super();
  }

}
