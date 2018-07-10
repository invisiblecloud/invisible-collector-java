package com.ic.invoicecapture.response.validators;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.ic.invoicecapture.connection.response.validators.IValidator;

import com.ic.invoicecapture.connection.response.validators.ValidatorComposite;
import com.ic.invoicecapture.exceptions.IcException;

public class ValidatorCompositeTest {
  
  @Test
  public void validate_empty() {
    ValidatorComposite composite = new ValidatorComposite();
    Assertions.assertThrows(IllegalArgumentException.class, composite::validateAndTryThrowException);
  }
  
  private IValidator prepareValidatorMock(boolean isValid) throws IcException {
    IValidator validator = EasyMock.createNiceMock(IValidator.class);
    validator.validateAndTryThrowException();
    if(isValid) {
      EasyMock.expectLastCall().once();
    } else {
      IcException exception = new IcException();
      EasyMock.expectLastCall().andThrow(exception);
    }
    
    EasyMock.replay(validator);
    return validator;
  }
  
  @Test
  public void validate_passing() throws IcException {
    ValidatorComposite composite = new ValidatorComposite();
    IValidator validValidator = this.prepareValidatorMock(true);
    IValidator validValidator2 = this.prepareValidatorMock(true);
    composite.addValidator(validValidator);
    composite.addValidator(validValidator2);
    
    composite.validateAndTryThrowException();
    
    EasyMock.verify(validValidator);
    EasyMock.verify(validValidator2);
  }
  
  @Test
  public void validate_failingCorrectOrder() throws IcException {
    ValidatorComposite composite = new ValidatorComposite();
    IValidator validValidator = this.prepareValidatorMock(true);
    IValidator failingValidator = this.prepareValidatorMock(false);
    composite.addValidator(validValidator);
    composite.addValidator(failingValidator);
    
    Assertions.assertThrows(IcException.class, composite::validateAndTryThrowException);
    
    EasyMock.verify(validValidator);
    EasyMock.verify(failingValidator);
  }
  
  
  
  

}
