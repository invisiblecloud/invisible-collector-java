package com.ic.invisiblecollector.connection.response.validators;

import com.ic.invisiblecollector.connection.response.IServerResponse;
import com.ic.invisiblecollector.exceptions.IcException;

public interface IValidator {
  /**
   * Throws exception if invalid.
   * Implementing classes should be immutable.
   * @throws IcException exception that describes what went wrong in the 'invalid' case
   */
  void assertValidResponse(IServerResponse responsePair) throws IcException;
}
