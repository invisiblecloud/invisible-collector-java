package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.exceptions.IcException;

public interface IValidator {
  /**
   * Throws exception if invalid.
   * @throws IcException
   */
  void validateAndTryThrowException() throws IcException;
}
