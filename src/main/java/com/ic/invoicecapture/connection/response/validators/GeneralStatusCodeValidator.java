package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

/**
 * Immutable class.
 * @author ros
 *
 */
public class GeneralStatusCodeValidator extends JsonValidatorBase implements IValidator {

  @Override
  public void validateAndTryThrowException(IServerResponse responseStatus) throws IcException {
    final int statusCode = responseStatus.getStatusCode();
    final int statusFamily = statusCode / 100;
    if (statusFamily != 2) {
      String errorBody = responseStatus.consumeConnectionAsString();
      if (isJsonResponse(responseStatus)) {
        String msg = buildServerErrorFacade(errorBody).getErrorMessage();
        throw new IcException(msg + " (statusCode: " + statusCode + ")");
      } else {
        throw new IcException("Server returned: " + statusCode + " "
            + responseStatus.getStatusCodeReasonPhrase() + "\n" + errorBody);
      }
    }
  }
}
