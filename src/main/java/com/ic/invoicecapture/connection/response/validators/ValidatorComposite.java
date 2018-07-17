package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;
import java.util.ArrayList;
import java.util.List;

/**
 * IResponseValidator order matters.
 * 
 * @author ros
 *
 */
public class ValidatorComposite implements IValidator {
  private List<IValidator> validators = new ArrayList<>();

  public ValidatorComposite addValidator(IValidator validator) {
    this.validators.add(validator);
    return this;
  }
  
  public ValidatorComposite addValidatorFirst(IValidator validator) {
    this.validators.add(0, validator);
    return this;
  }

  @Override
  public void validateAndTryThrowException(IServerResponse serverResponse) throws IcException {

    for (IValidator validator : this.validators) {
      validator.validateAndTryThrowException(serverResponse);
    }
  }

}
