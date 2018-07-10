package com.ic.invoicecapture.response.validators;


import org.junit.jupiter.api.Assertions;
import com.ic.invoicecapture.connection.response.validators.ValidationResult;

public class ValidationBase {
  
  public void assertValid(ValidationResult validationResult) {
    Assertions.assertEquals(validationResult.isValid(), true);
    Assertions.assertEquals(validationResult.getException(), null);
  }
  
  public void assertNotValid(ValidationResult validationResult) {
    Assertions.assertEquals(validationResult.isValid(), false);
    Assertions.assertNotEquals(validationResult.getException(), null);
  }

}
