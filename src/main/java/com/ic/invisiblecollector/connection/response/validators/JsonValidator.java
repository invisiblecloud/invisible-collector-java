package com.ic.invisiblecollector.connection.response.validators;

import com.ic.invisiblecollector.connection.response.IServerResponse;
import com.ic.invisiblecollector.exceptions.IcException;

/**
 * Immutable class.
 * 
 * @author ros
 *
 */
public class JsonValidator extends JsonValidatorBase implements IValidator {

  @Override
  public void assertValidResponse(IServerResponse serverResponse) throws IcException {
    if (!this.isJsonResponse(serverResponse)) {
      throw new IcException("Expected json response from server");
    }
  }
}
