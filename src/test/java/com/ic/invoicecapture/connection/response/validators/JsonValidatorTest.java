package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.validators.JsonValidator;
import com.ic.invoicecapture.exceptions.IcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonValidatorTest {

  @Test
  public void validate_success() throws IcException {
    JsonValidator jsonValidator = new JsonValidator("text/plain, application/json");

    jsonValidator.validateAndTryThrowException();
  }

  @Test
  public void validate_fail() {
    final String bodyString = "abcde";

    JsonValidator jsonValidator = new JsonValidator(bodyString);
    IcException exception =
        Assertions.assertThrows(IcException.class, jsonValidator::validateAndTryThrowException);

    String exceptionMessage = exception.getMessage();
    Assertions.assertTrue(exceptionMessage.contains(bodyString));
  }
}
