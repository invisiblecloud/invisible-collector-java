package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

/**
 * Immutable class.
 * 
 * @author ros
 *
 */
public class JsonValidator extends JsonValidatorBase implements IValidator {

  @Override
  public void validateAndTryThrowException(IServerResponse serverResponse) throws IcException {
    if (!this.isJsonResponse(serverResponse)) {
      throw new IcException("Expected json response from server");
    }
  }
}
