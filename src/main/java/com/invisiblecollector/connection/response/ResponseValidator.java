package com.invisiblecollector.connection.response;

import com.invisiblecollector.exceptions.IcConflictingException;
import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.json.JsonModelFacade;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public class ResponseValidator {
  private final JsonModelFacade jsonFacade;

  public ResponseValidator(JsonModelFacade jsonFacade) {

    this.jsonFacade = jsonFacade;
  }

  public void validate(Response response) throws IcException {
    assertJsonResponse(response);

    if (isValidStatusCode(response)) {
      return;
    }

    InputStream is = response.readEntity(InputStream.class);
    ErrorObject errObj = jsonFacade.parseStringStream(is, ErrorObject.class);
    throwException(errObj);
  }

  private void throwException(ErrorObject errObj) throws IcException {
    if (errObj.getCode() == null || errObj.getMessage() == null) {
      throw new IcException("Invalid error JSON returned");
    }

    String msg = errObj.getMessage() + " (status code: " + errObj.getCode() + ")";
    if (errObj.hasGid()) {
      throw new IcConflictingException(msg, errObj.getGid());
    }

    throw new IcException(msg);
  }

  private boolean isValidStatusCode(Response response) {
    int status = response.getStatus();
    int family = status / 100;

    if (family == 1 || family == 2 || family == 3) {
      return true;
    }

    return false;
  }

  private void assertJsonResponse(Response response) throws IcException {
    String contentTypeHeader = response.getHeaderString("Content-Type");
    if (contentTypeHeader == null || !contentTypeHeader.contains(MediaType.APPLICATION_JSON)) {
      throw new IcException(
          "Expected JSON response from server. Returned status code: " + response.getStatus());
    }
  }
}
