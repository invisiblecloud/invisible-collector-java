package com.ic.invisiblecollector.model;

public interface ICheckableField {
  void assertValueIsValid(Object value) throws IllegalArgumentException;
  
}
