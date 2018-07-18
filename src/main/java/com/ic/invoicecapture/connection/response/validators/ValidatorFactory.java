package com.ic.invoicecapture.connection.response.validators;

public class ValidatorFactory {
  
  private static final IValidator ERROR_401_VALIDATOR = new SpecificStatusCodeValidator(401,
      "Invalid API token supplied, was rejected by the server");
  private static final IValidator ERROR_422_VALIDATOR = new SpecificStatusCodeValidator(422,
      "Invalid json sent to the server (probably a library error)");
  private static final IValidator ERROR_404_VALIDATOR = new SpecificStatusCodeValidator(404,
      "requested entity Not Found");
  
  private ValidatorComposite buildCommonComposite() {
    return new ValidatorComposite()
        .addValidatorLast(ERROR_401_VALIDATOR)
        .addValidatorLast(new StatusCodeValidator())
        .addValidatorLast(new JsonValidator());
  }
  
  private ValidatorComposite buildConflictValidatorComposite() {
    return buildCommonComposite()
    .addValidatorFirst(
        new GidConflictValidator("Entity already exists with the same VAT number or externalId"))
    .addValidatorFirst(ERROR_422_VALIDATOR);
  }

  public IValidator buildBasicValidator() {
    return buildCommonComposite();
  }

  public IValidator buildConflictValidator() {
    return buildConflictValidatorComposite();
  }
  
  public IValidator buildJsonValidator() {
    return buildCommonComposite()
        .addValidatorFirst(ERROR_422_VALIDATOR);
  }
  
  public IValidator buildExistingEntityValidator() {
    return buildCommonComposite()
        .addValidatorFirst(ERROR_404_VALIDATOR);
  }
  
  public IValidator buildExistingConflictingEntityValidator() {
    return buildConflictValidatorComposite()
        .addValidatorFirst(ERROR_404_VALIDATOR);
  }
}
