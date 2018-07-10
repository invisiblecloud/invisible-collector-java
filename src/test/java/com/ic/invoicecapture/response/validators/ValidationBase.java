package com.ic.invoicecapture.response.validators;


import org.junit.jupiter.api.Assertions;
import com.ic.invoicecapture.connection.response.validators.ValidationResult;

public class ValidationBase {
  
  public void assertValid(ValidationResult validationResult) {
    Assertions.assertEquals(true, validationResult.isValid());
    Assertions.assertEquals(null, validationResult.getException());
  }
  
  public void assertNotValid(ValidationResult validationResult) {
    Assertions.assertEquals(false, validationResult.isValid());
    Assertions.assertNotEquals(null, validationResult.getException());
  }

}
