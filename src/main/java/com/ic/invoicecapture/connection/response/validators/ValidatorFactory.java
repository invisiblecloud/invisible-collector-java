package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.RequestType;
import com.ic.invoicecapture.connection.response.ServerResponse;

public class ValidatorFactory {

  public IValidator build(RequestType requestType, ServerResponse responsePair) {
    switch (requestType) {
      case GET:
        return new StatusCodeValidator(responsePair);
      default:
        throw new UnsupportedOperationException();
    }
  }

}
