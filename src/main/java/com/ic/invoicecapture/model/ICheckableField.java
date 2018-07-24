package com.ic.invoicecapture.model;

public interface ICheckableField {
  void assertValueIsValid(Object value) throws IllegalArgumentException;
  
}
