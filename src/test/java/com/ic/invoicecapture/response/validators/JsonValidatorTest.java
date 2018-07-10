package com.ic.invoicecapture.response.validators;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.ic.invoicecapture.connection.response.validators.JsonValidator;

import com.ic.invoicecapture.exceptions.IcException;

public class JsonValidatorTest {

  @Test
  public void validate_pass() throws IcException {
    JsonValidator jsonValidator = new JsonValidator("text/plain, application/json");
    
    jsonValidator.validateAndTryThrowException();
  }
  
  @Test
  public void validate_fail() {
    final String BODY_STRING = "abcde";
    
    JsonValidator jsonValidator = new JsonValidator(BODY_STRING);
    IcException exception = Assertions.assertThrows(IcException.class, jsonValidator::validateAndTryThrowException);
    
    String exceptionMessage = exception.getMessage();
    Assertions.assertTrue(exceptionMessage.contains(BODY_STRING));
  }
}
