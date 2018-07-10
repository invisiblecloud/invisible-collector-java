package com.ic.invoicecapture.response.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.ic.invoicecapture.connection.response.validators.ValidationResult;
import com.ic.invoicecapture.exceptions.IcException;

public class ValidationResultTest extends ValidationBase {


  @Test
  public void constructor_failure() {

    IcException testException = new IcException("Test");
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new ValidationResult(true, testException));
    
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new ValidationResult(false, null)); 
  }
  
  @Test
  public void buildPassingCorrectness() {
    this.assertValid(ValidationResult.buildPassing());
  }
  
  @Test
  public void tryThrowException_throwing() {
    IcException testException = new IcException("Test");
    ValidationResult result = new ValidationResult(false, testException);
    IcException thrown = Assertions.assertThrows(IcException.class, result::tryThrowException);
    Assertions.assertEquals(testException, thrown);
  }
  
  @Test
  public void tryThrowException_nonThrowing() throws IcException {
    ValidationResult result = new ValidationResult(true, null);
    result.tryThrowException();
  }
  
  
}
