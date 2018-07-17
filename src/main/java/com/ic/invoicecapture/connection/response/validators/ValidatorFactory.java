package com.ic.invoicecapture.connection.response.validators;

public class ValidatorFactory {

  private ValidatorComposite buildCommonComposite() {
    ValidatorComposite composite = new ValidatorComposite();
    composite.addValidator(new StatusCodeValidator());
    composite.addValidator(new JsonValidator());
    return composite;
  }

  public IValidator buildCompanyReturnValidator() {
    return buildCommonComposite().addValidatorFirst(new SpecificStatusCodeValidator(401,
        "Invalid API token supplied, was rejected by the server"));
  }
}
