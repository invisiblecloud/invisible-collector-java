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

  public void addValidator(IValidator validator) {
    this.validators.add(validator);
  }

  @Override
  public void validateAndTryThrowException(IServerResponse serverResponse) throws IcException {
    
    if (this.validators.isEmpty()) {
      throw new IllegalArgumentException("No validators added");
    }
    
    for (IValidator validator : this.validators) {
      validator.validateAndTryThrowException(serverResponse);
    }
  }

}
