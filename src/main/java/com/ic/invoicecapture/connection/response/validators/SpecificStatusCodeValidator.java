package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

public class SpecificStatusCodeValidator implements IValidator {

  private final int badStatusCode;
  private final String exceptionMsg;

  public SpecificStatusCodeValidator(int badStatusCode, String exceptionMsg) {
    this.badStatusCode = badStatusCode;
    this.exceptionMsg = exceptionMsg;
   
  }
  
  @Override
  public void validateAndTryThrowException(IServerResponse responsePair) throws IcException {
    final int statusCode = responsePair.getStatusCode();
    if (statusCode == this.badStatusCode) {
      throw new IcException(this.exceptionMsg);
    }
  }

}
