package com.ic.invoicecapture.response.validators;

import org.junit.Assert;
import org.junit.Test;
import com.ic.invoicecapture.connection.response.validators.JsonValidator;
import com.ic.invoicecapture.connection.response.validators.ValidationResult;
import com.ic.invoicecapture.exceptions.IcException;

public class JsonValidatorTest {

  @Test
  public void validate_pass() {
    JsonValidator jsonValidator = new JsonValidator("text/plain, application/json");
    
    ValidationResult validationResult = jsonValidator.validate();
    
    Assert.assertEquals(validationResult.isValid(), true);
    Assert.assertEquals(validationResult.getException(), null);
  }
  
  @Test
  public void validate_fail() {
    final String BODY_STRING = "abcde";
    
    JsonValidator jsonValidator = new JsonValidator(BODY_STRING);
    
    ValidationResult validationResult = jsonValidator.validate();
    
    IcException exception = validationResult.getException();
    Assert.assertEquals(validationResult.isValid(), false);
    Assert.assertNotEquals(exception, null);
    
    String exceptionMessage = exception.getMessage();
    Assert.assertTrue(exceptionMessage.contains(BODY_STRING));
  }
}
