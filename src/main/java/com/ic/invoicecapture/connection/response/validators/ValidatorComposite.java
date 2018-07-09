package com.ic.invoicecapture.connection.response.validators;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
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
  public ValidationResult validate() {
    for (IValidator validator : this.validators) {
      ValidationResult validationResult = validator.validate();
      if (!validationResult.isValid()) {
        return validationResult;
      }
    }

    return ValidationResult.buildPassing();
  }

}
