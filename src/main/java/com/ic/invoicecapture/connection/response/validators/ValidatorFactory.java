package com.ic.invoicecapture.connection.response.validators;

public class ValidatorFactory {
  
  public IValidator buildCompanyReturnValidator() {
    ValidatorComposite composite = new ValidatorComposite();
    composite.addValidator(new StatusCodeValidator());
    composite.addValidator(new JsonValidator());
    return composite;
  }
}
