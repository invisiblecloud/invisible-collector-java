package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

public class StatusCodeValidator implements IValidator {

  @Override
  public void validateAndTryThrowException(IServerResponse responseStatus) throws IcException {
    final int statusCode = responseStatus.getStatusCode();
    final int statusFamily = statusCode / 100;
    if (statusFamily != 2) {
      String exceptionMsg =
          "Server returned: " + statusCode + " " + responseStatus.getStatusCodeReasonPhrase();
      try {
        exceptionMsg += "\n" + responseStatus.consumeConnectionAsString();
        throw new IcException(exceptionMsg);
      } catch (IcException e) {
        throw new IcException(exceptionMsg, e);
      }
    } 
  }
}
