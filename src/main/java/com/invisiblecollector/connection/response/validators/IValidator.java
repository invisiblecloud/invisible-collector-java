package com.invisiblecollector.connection.response.validators;

import com.invisiblecollector.connection.response.IServerResponse;
import com.invisiblecollector.exceptions.IcException;

public interface IValidator {
  /**
   * Throws exception if invalid.
   * Implementing classes should be immutable.
   * @throws IcException exception that describes what went wrong in the 'invalid' case
   */
  void assertValidResponse(IServerResponse responsePair) throws IcException;
}
