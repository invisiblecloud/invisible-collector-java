package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IServerResponse;
import com.ic.invoicecapture.connection.response.validators.StatusCodeValidator;
import com.ic.invoicecapture.exceptions.IcException;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatusCodeValidatorTest  {

  protected IServerResponse buildResponseStatusMock(int code, String reason, String body)
      throws IcException {
    return new ServerResponseMockBuilder()
        .setStatusCode(code)
        .setReason(reason)
        .setBody(body)
        .build();
  }
  
  @Test
  public void validate_success() throws IcException {
    IServerResponse status = this.buildResponseStatusMock(200, "OK", "body");

    new StatusCodeValidator()
        .validateAndTryThrowException(status);
  }

  @Test
  public void validate_fail() throws IcException {
    final int statusCode = 400;
    final String reasonMsg = "reason";
    final String boyMsg = "body string";

    IServerResponse status = this.buildResponseStatusMock(statusCode, reasonMsg, boyMsg);

    StatusCodeValidator statusCodeValidator = new StatusCodeValidator();

    IcException exception = Assertions.assertThrows(IcException.class,
        ()->statusCodeValidator.validateAndTryThrowException(status));


    String exceptionMessage = exception.getMessage();
    Assertions.assertTrue(exceptionMessage.contains("" + statusCode));
    Assertions.assertTrue(exceptionMessage.contains(reasonMsg));
    Assertions.assertTrue(exceptionMessage.contains(boyMsg));
  }

}
