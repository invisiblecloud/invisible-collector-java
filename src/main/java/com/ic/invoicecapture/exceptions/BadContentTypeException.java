package com.ic.invoicecapture.exceptions;

public class BadContentTypeException extends ICIOxception {
  public BadContentTypeException(String received, String expected) {
    super("Wrong content-type received, expected: " + expected + " received: " + received);
  }
}
