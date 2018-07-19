package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;
import java.util.ArrayList;
import java.util.List;

/**
 * IResponseValidator order matters.
 * Immutable class.
 */
public class ValidatorComposite implements IValidator {
  private List<IValidator> validators;

  public ValidatorComposite() {
    this.validators = new ArrayList<>();
  }
  
  public ValidatorComposite(List<IValidator> validators) {
    this.validators = validators;
  }

  

  @Override
  public void validateAndTryThrowException(IServerResponse serverResponse) throws IcException {

    for (IValidator validator : this.validators) {
      validator.validateAndTryThrowException(serverResponse);
    }
  }

}
