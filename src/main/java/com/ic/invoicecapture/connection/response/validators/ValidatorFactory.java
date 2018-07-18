package com.ic.invoicecapture.connection.response.validators;

public class ValidatorFactory {

  private ValidatorComposite buildCommonComposite() {
    return new ValidatorComposite()
        .addValidatorLast(new SpecificStatusCodeValidator(401,
        "Invalid API token supplied, was rejected by the server"))
        .addValidatorLast(new StatusCodeValidator())
        .addValidatorLast(new JsonValidator());
  }
  
  private ValidatorComposite buildConflictValidatorComposite() {
    return buildCommonComposite()
    .addValidatorFirst(
        new GidConflictValidator("Entity already exists with the same VAT number or externalId"))
    .addValidatorFirst(new SpecificStatusCodeValidator(422,
        "Invalid json sent to the server (probably a library error)"));
  }

  public IValidator buildBasicValidator() {
    return buildCommonComposite();
  }

  public IValidator buildConflictValidator() {
    return buildConflictValidatorComposite();
  }
  
  public IValidator buildExistingEntityValidator() {
    return buildCommonComposite()
        .addValidatorFirst(new SpecificStatusCodeValidator(404,
            "requested entity Not Found"));
  }
  
  public IValidator buildExistingConflictingEntityValidator() {
    return buildConflictValidatorComposite()
        .addValidatorFirst(new SpecificStatusCodeValidator(404,
            "requested entity Not Found"));
  }
}
