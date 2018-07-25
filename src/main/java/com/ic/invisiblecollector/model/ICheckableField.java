package com.ic.invisiblecollector.model;

public interface ICheckableField {
  /**
   * Assert the {@link Object} value corresponding to the attribute is 
   * valid (in type, value, etc).
   * 
   * @param value the value to validate
   * @throws IllegalArgumentException thrown in case the value isn't valid.
   */
  void assertValueIsValid(Object value) throws IllegalArgumentException;
}
