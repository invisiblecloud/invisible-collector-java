package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.exceptions.IcException;
import org.apache.http.HttpEntity;

public class JsonValidator implements IValidator {

  private static final String HEADER_NAME = "Content-Type";
  private static final String JSON_CONTENT_TYPE = "application/json";

  private String contentType;

  public JsonValidator(ServerResponseFacade serverResponse) {
    this.contentType = serverResponse.getHeaderValues(HEADER_NAME);
  }

  public JsonValidator(String contentType) {
    this.contentType = contentType;
  }

  @Override
  public void validateAndTryThrowException() throws IcException {
    if (this.contentType == null) {
      throw new IcException("Expected a " + HEADER_NAME + " header in server response");
    }

    if (!this.contentType.contains(JSON_CONTENT_TYPE)) {
      final String exceptionMessage =
          String.format("Wrong content-type received, expected: '%s', received: '%s'",
              JSON_CONTENT_TYPE, this.contentType);
      throw new IcException(exceptionMessage);
    }
  }
}
