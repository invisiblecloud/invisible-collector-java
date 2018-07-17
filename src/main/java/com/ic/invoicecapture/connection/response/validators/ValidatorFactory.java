package com.ic.invoicecapture.connection.response.validators;

public class ValidatorFactory {

  private ValidatorComposite buildCommonComposite() {
    ValidatorComposite composite = new ValidatorComposite();
    composite.addValidatorLast(new SpecificStatusCodeValidator(401,
        "Invalid API token supplied, was rejected by the server"));
    composite.addValidatorLast(new StatusCodeValidator());
    composite.addValidatorLast(new JsonValidator());
    return composite;
  }

  public IValidator buildCompanyReturnValidator() {
    return buildCommonComposite();
  }

  public IValidator buildCustomerValidator() {
    return buildCommonComposite()
        .addValidatorFirst(
            new GidConflictValidator("Customer already exists with the same VAT number"))
        .addValidatorFirst(new SpecificStatusCodeValidator(422,
            "Invalid json sent to the server (probably a library error)"));

  }
}
