package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.exceptions.IcException;
import org.apache.http.StatusLine;

public class StatusCodeValidator implements IValidator {

  private ServerResponse responsePair;

  public StatusCodeValidator(ServerResponse responsePair) {
    this.responsePair = responsePair;
  }

  @Override
  public ValidationResult validate() {
    StatusLine statusLine = responsePair.getStatusLine();
    final int statusCode = statusLine.getStatusCode();
    final int statusFamily = statusCode / 100;
    if (statusFamily != 2) {
      String exceptionMsg =
          "Status code returned: " + statusCode + " " + statusLine.getReasonPhrase();
      try {
        exceptionMsg += "\n" + this.responsePair.getBodyAsString();
      } catch (IcException e) {
        exceptionMsg += "\n" + e.getMessage();
      }
      return new ValidationResult(false, new IcException(exceptionMsg));
    } else {
      return ValidationResult.buildPassing();
    }
  }
}
