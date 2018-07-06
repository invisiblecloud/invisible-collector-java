package com.ic.invoicecapture.connection.response.validators;


import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import com.ic.invoicecapture.exceptions.ICIOxception;

/**
 * IResponseValidator order matters
 * 
 * @author ros
 *
 */
public class ValidatorComposite implements IResponseValidator {
  private List<IResponseValidator> validators = new ArrayList<>();

  public void addValidator(IResponseValidator validator) {
    this.validators.add(validator);
  }

  @Override
  public Pair<Boolean, ? extends ICIOxception> validate() {
    for (IResponseValidator validator : this.validators) {
      Pair<Boolean, ? extends ICIOxception> validationPair = validator.validate();
      if (!validationPair.getValue0()) {
        return validationPair;
      }
    }

    return Pair.with(false, null);
  }

}
