package com.invisiblecollector.connection.response.validators;

import com.invisiblecollector.connection.response.IServerResponse;
import com.invisiblecollector.exceptions.IcException;

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
