package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

public class SpecificStatusCodeValidator extends JsonValidatorBase implements IValidator {

  private final int badStatusCode;
  protected final String exceptionMsg;

  public SpecificStatusCodeValidator(int badStatusCode, String exceptionMsg) {
    this.badStatusCode = badStatusCode;
    this.exceptionMsg = exceptionMsg;

  }
  
  protected void throwJsonException(ServerErrorFacade serverErrorFacade) throws IcException {
    String msg = serverErrorFacade.getErrorMessage();
    throw new IcException(this.exceptionMsg + " (\"" + msg + "\")");
  }

  @Override
  public void validateAndTryThrowException(IServerResponse responsePair) throws IcException {
    final int statusCode = responsePair.getStatusCode();
    if (statusCode == this.badStatusCode) {
      if (isJsonResponse(responsePair)) {
        String errorBody = responsePair.consumeConnectionAsString();
        ServerErrorFacade serverErrorFacade = new ServerErrorFacade(errorBody);
        throwJsonException(serverErrorFacade);
      } else {
        throw new IcException(this.exceptionMsg);
      }
    }
  }

}