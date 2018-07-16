package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.IResponseStatus;
import com.ic.invoicecapture.connection.response.validators.StatusCodeValidator;
import com.ic.invoicecapture.exceptions.IcException;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatusCodeValidatorTest {

  private IResponseStatus buildResponseStatusMock(int code, String reason, String body)
      throws IcException {
    IResponseStatus mock = EasyMock.createNiceMock(IResponseStatus.class);
    EasyMock.expect(mock.getStatusCode()).andReturn(code);
    EasyMock.expect(mock.getStatusCodeReasonPhrase()).andReturn(reason);
    EasyMock.expect(mock.consumeConnectionAsString()).andReturn(body);

    EasyMock.replay(mock);
    return mock;
  }


  @Test
  public void validate_success() throws IcException {
    IResponseStatus status = this.buildResponseStatusMock(200, "OK", "body");

    StatusCodeValidator statusCodeValidator = new StatusCodeValidator(status);

    statusCodeValidator.validateAndTryThrowException();
  }

  @Test
  public void validate_fail() throws IcException {
    final int statusCode = 400;
    final String reasonMsg = "reason";
    final String boyMsg = "body string";

    IResponseStatus status = this.buildResponseStatusMock(statusCode, reasonMsg, boyMsg);

    StatusCodeValidator statusCodeValidator = new StatusCodeValidator(status);

    IcException exception = Assertions.assertThrows(IcException.class,
        statusCodeValidator::validateAndTryThrowException);


    String exceptionMessage = exception.getMessage();
    Assertions.assertTrue(exceptionMessage.contains("" + statusCode));
    Assertions.assertTrue(exceptionMessage.contains(reasonMsg));
    Assertions.assertTrue(exceptionMessage.contains(boyMsg));
  }

}
