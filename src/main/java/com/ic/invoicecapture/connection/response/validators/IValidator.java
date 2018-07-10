package com.ic.invoicecapture.connection.response.validators;

public interface IValidator {
  /**
   * Checks for response validity.
   * 
   * @return a pair where the boolean as true means the response is valid and as false means it's
   *         not valid and the exception contains the reason
   */
  ValidationResult validate();
}
