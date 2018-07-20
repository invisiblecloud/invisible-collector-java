package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.exceptions.IcException;

/**
 * Immutable class.
 * @author ros
 *
 */
public class JsonValidatorBase {
  protected static final String HEADER_NAME = "Content-Type";
  protected static final String JSON_CONTENT_TYPE = "application/json";

  protected boolean isJsonResponse(IServerResponse serverResponse) {
    String contentType = serverResponse.getHeaderValues(HEADER_NAME);
    if (contentType == null) {
      return false;
    } else {
      return contentType.contains(JSON_CONTENT_TYPE);
    }
  }
  
  protected ServerErrorFacade buildServerErrorFacade(String errorJson) throws IcException {
    
    try {
      return new ServerErrorFacade(errorJson);
    } catch (IllegalArgumentException e) {
      throw new IcException("Invalid json response received from server", e);
    }
  }
  
}
