package com.ic.invoicecapture.connection.response.validators;

import java.util.ArrayList;
import java.util.List;
import com.ic.invoicecapture.exceptions.IcException;

/**
 * IResponseValidator order matters.
 * 
 * @author ros
 *
 */
public class ValidatorComposite implements IValidator {
  private List<IValidator> validators = new ArrayList<>();

  public void addValidator(IValidator validator) {
    this.validators.add(validator);
  }

  @Override
  public void validateAndTryThrowException() throws IcException {
    
    if (this.validators.isEmpty()) {
      throw new IllegalArgumentException("No validators added");
    }
    
    for (IValidator validator : this.validators) {
      validator.validateAndTryThrowException();
    }
  }

}
