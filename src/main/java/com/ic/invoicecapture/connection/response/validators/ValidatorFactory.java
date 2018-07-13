package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.RequestType;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;

public class ValidatorFactory {

  public IValidator build(RequestType requestType, ServerResponseFacade responsePair) {
    switch (requestType) {
      case GET:
        ValidatorComposite composite = new ValidatorComposite();
        composite.addValidator(new StatusCodeValidator(responsePair));
        composite.addValidator(new JsonValidator(responsePair));
        return composite;
      default:
        throw new UnsupportedOperationException();
    }
  }

}
