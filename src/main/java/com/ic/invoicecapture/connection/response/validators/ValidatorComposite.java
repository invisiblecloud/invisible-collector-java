package com.ic.invoicecapture.connection.response.validators;

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
  public ValidationResult validate() {
    
    if (this.validators.isEmpty()) {
      throw new IllegalArgumentException("No validators added");
    }
    
    for (IValidator validator : this.validators) {
      ValidationResult validationResult = validator.validate();
      if (!validationResult.isValid()) {
        return validationResult;
      }
    }

    return ValidationResult.buildPassing();
  }

}
