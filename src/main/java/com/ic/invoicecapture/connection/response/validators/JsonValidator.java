package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

public class JsonValidator implements IValidator {

  private static final String HEADER_NAME = "Content-Type";
  private static final String JSON_CONTENT_TYPE = "application/json";

  @Override
  public void validateAndTryThrowException(IServerResponse serverResponse) throws IcException {
    String contentType = serverResponse.getHeaderValues(HEADER_NAME);
    if (contentType == null) {
      throw new IcException("Expected a " + HEADER_NAME + " header in server response");
    }

    if (!contentType.contains(JSON_CONTENT_TYPE)) {
      final String exceptionMessage =
          String.format("Wrong content-type received, expected: '%s', received: '%s'",
              JSON_CONTENT_TYPE, contentType);
      throw new IcException(exceptionMessage);
    }
  }
}
