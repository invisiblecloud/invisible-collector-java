package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorComposite;
import com.ic.invoicecapture.exceptions.IcException;
import java.util.Arrays;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidatorCompositeTest {

  private IServerResponse serverResponse;

  private IValidator prepareValidatorMock(boolean isValid) throws IcException {
    IValidator validator = EasyMock.createNiceMock(IValidator.class);
    validator.validateAndTryThrowException(this.serverResponse);
    if (isValid) {
      EasyMock.expectLastCall().once();
    } else {
      IcException exception = new IcException();
      EasyMock.expectLastCall().andThrow(exception).once();
    }

    EasyMock.replay(validator);
    return validator;
  }

  @BeforeEach
  private void initMocks() throws IcException {
    this.serverResponse = new ServerResponseMockBuilder().build();
  }

  @Test
  public void validate_empty() throws IcException {
    ValidatorComposite composite = new ValidatorComposite();
    composite.validateAndTryThrowException(this.serverResponse);
  }
  
  private ValidatorComposite buildComposite(IValidator... validators) {
    return new ValidatorComposite(Arrays.asList(validators));
  }

  @Test
  public void validate_successing() throws IcException {
    IValidator validValidator = this.prepareValidatorMock(true);
    IValidator validValidator2 = this.prepareValidatorMock(true);
    ValidatorComposite composite = buildComposite(validValidator, validValidator2);

    composite.validateAndTryThrowException(this.serverResponse);

    EasyMock.verify(validValidator);
    EasyMock.verify(validValidator2);
  }

  @Test
  public void validate_failingCorrectOrder() throws IcException {
    IValidator validValidator = this.prepareValidatorMock(true);
    IValidator failingValidator = this.prepareValidatorMock(false);
    ValidatorComposite composite = buildComposite(validValidator, failingValidator);

    Assertions.assertThrows(IcException.class,
        () -> composite.validateAndTryThrowException(this.serverResponse));

    EasyMock.verify(validValidator);
    EasyMock.verify(failingValidator);
  }



}
