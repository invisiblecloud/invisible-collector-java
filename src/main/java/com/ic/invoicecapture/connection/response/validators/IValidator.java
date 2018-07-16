package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

public interface IValidator {
  /**
   * Throws exception if invalid.
   * @throws IcException exception that describes what went wrong in the 'invalid' case
   */
  void validateAndTryThrowException(IServerResponse responsePair) throws IcException;
}
