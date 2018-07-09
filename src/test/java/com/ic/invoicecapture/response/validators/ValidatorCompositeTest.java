package com.ic.invoicecapture.response.validators;

import javax.xml.validation.Validator;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidationResult;
import com.ic.invoicecapture.connection.response.validators.ValidatorComposite;
import com.ic.invoicecapture.exceptions.IcException;

public class ValidatorCompositeTest extends ValidationBase {
  
  @Test
  public void validate_empty() {
    ValidatorComposite composite = new ValidatorComposite();
    Assertions.assertThrows(IllegalArgumentException.class, composite::validate);
  }
  
  private static final String EXCEPTION_MESSAGE = "test exception";
  private ValidationResult prepareValidationResultMock(boolean isValid) {
    ValidationResult validationResult = EasyMock.createNiceMock(ValidationResult.class);
    EasyMock.expect(validationResult.isValid()).andReturn(isValid);
    EasyMock.replay(validationResult);
    return validationResult;
  }
  
  private IValidator prepareValidatorMock(ValidationResult validationResult) {
    IValidator validator = EasyMock.createNiceMock(IValidator.class);
    EasyMock.expect(validator.validate()).andReturn(validationResult);
    EasyMock.replay(validator);
    
    return validator;
  }
  
  @Test
  public void validate_passing() {
    ValidatorComposite composite = new ValidatorComposite();
    ValidationResult validResult = this.prepareValidationResultMock(true);
    IValidator validValidator = this.prepareValidatorMock(validResult);
    ValidationResult validResult2 = this.prepareValidationResultMock(true);
    IValidator validValidator2 = this.prepareValidatorMock(validResult2);
    composite.addValidator(validValidator);
    composite.addValidator(validValidator2);
    
    ValidationResult returnedValidation = composite.validate();
    
    this.assertValid(returnedValidation);
    
    EasyMock.verify(validResult);
    EasyMock.verify(validValidator);
    EasyMock.verify(validResult2);
    EasyMock.verify(validValidator2);
  }
  
  @Test
  public void validate_failingCorrectOrder() {
    ValidatorComposite composite = new ValidatorComposite();
    ValidationResult validResult = this.prepareValidationResultMock(true);
    IValidator validValidator = this.prepareValidatorMock(validResult);
    ValidationResult failingResult = this.prepareValidationResultMock(false);
    IValidator failingValidator = this.prepareValidatorMock(failingResult);
    composite.addValidator(validValidator);
    composite.addValidator(failingValidator);
    
    ValidationResult returnedValidation = composite.validate();
    
    Assertions.assertEquals(failingResult, returnedValidation);
    
    EasyMock.verify(validResult);
    EasyMock.verify(validValidator);
    EasyMock.verify(failingResult);
    EasyMock.verify(failingValidator);
  }
  
  
  
  

}
