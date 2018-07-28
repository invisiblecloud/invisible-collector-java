package com.invisiblecollector.connection.response.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.invisiblecollector.connection.response.IServerResponse;
import com.invisiblecollector.exceptions.IcException;

public class JsonValidatorTest {

  private static final String TARGET_HEADER = "Content-Type";
  private static final String HEADER_VALUE = "text/plain, application/json";

  private IServerResponse buildResponseMock(String headerName, String headerValue)
      throws IcException {
    return new ServerResponseMockBuilder().addHeader(headerName, headerValue).build();
  }

  @Test
  public void validate_success() throws IcException {
    JsonValidator jsonValidator = new JsonValidator();

    IServerResponse response = buildResponseMock(TARGET_HEADER, HEADER_VALUE);
    jsonValidator.assertValidResponse(response);
  }

  private String exceptionAssertion(String headerName, String headerValue) throws IcException {
    JsonValidator jsonValidator = new JsonValidator();

    IServerResponse response = buildResponseMock(headerName, headerValue);
    IcException exception = Assertions.assertThrows(IcException.class,
        () -> jsonValidator.assertValidResponse(response));

    return exception.getMessage();
  }

  @Test
  public void validate_missingHeader() throws IcException {
    this.exceptionAssertion("abd", HEADER_VALUE);
  }

  @Test
  public void validate_wrongHeaderValue() throws IcException {
    String headerValue = "abcde";
    this.exceptionAssertion(TARGET_HEADER, headerValue);
  }
}
