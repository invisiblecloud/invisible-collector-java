package com.ic.invoicecapture.response.validators;

import org.junit.Assert;
import com.ic.invoicecapture.connection.response.validators.ValidationResult;

public class ValidationBase {
  
  public void assertValid(ValidationResult validationResult) {
    Assert.assertEquals(validationResult.isValid(), true);
    Assert.assertEquals(validationResult.getException(), null);
  }
  
  public void assertNotValid(ValidationResult validationResult) {
    Assert.assertEquals(validationResult.isValid(), false);
    Assert.assertNotEquals(validationResult.getException(), null);
  }

}
