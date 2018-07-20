package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.exceptions.IcRuntimeException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Validator builder. Order of added validator checking is stack based (LIFO).
 * 
 * @author ros
 *
 */
public class ValidatorBuilder implements Cloneable {

  private static final IValidator ERROR_401_VALIDATOR = new SpecificStatusCodeValidator(401,
      "Invalid API token supplied, was rejected by the server");
  private static final IValidator ERROR_404_VALIDATOR =
      new SpecificStatusCodeValidator(404, "requested entity Not Found");
  private static final IValidator ERROR_422_VALIDATOR = new SpecificStatusCodeValidator(422,
      "Invalid json sent to the server (probably a library error)");

  private Stack<IValidator> validatorStack = new Stack<>();

  public ValidatorBuilder() {}
  
  public ValidatorBuilder(ValidatorBuilder validatorBuilder) {
    validatorStack.addAll(validatorBuilder.validatorStack);
  }

  public ValidatorBuilder addBadClientJsonValidator() {
    validatorStack.push(ERROR_422_VALIDATOR);
    return this;
  }
  
  public ValidatorBuilder addServerJsonValidator() {
    validatorStack.add(0, new JsonValidator());
    return this;
  }

  public ValidatorBuilder addCommonValidators() {
    validatorStack.push(new GeneralStatusCodeValidator());
    validatorStack.push(ERROR_401_VALIDATOR);
    validatorStack.push(ERROR_404_VALIDATOR);
    return this;
  }

  public ValidatorBuilder addConflictValidator() {
    validatorStack.push(
        new GidConflictValidator("Entity already exists with the same VAT number or externalId"));
    return this;
  }

  public IValidator build() {
    List<IValidator> validators = new ArrayList<>(validatorStack);
    Collections.reverse(validators);
    return new ValidatorComposite(validators);
  }

  public ValidatorBuilder clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      throw new IcRuntimeException("Shouldn't happen");
    }
    return new ValidatorBuilder(this);
  }
}
