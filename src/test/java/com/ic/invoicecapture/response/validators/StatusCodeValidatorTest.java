package com.ic.invoicecapture.response.validators;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ic.invoicecapture.connection.response.IResponseStatus;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.connection.response.validators.StatusCodeValidator;
import com.ic.invoicecapture.exceptions.IcException;

public class StatusCodeValidatorTest {

  private IResponseStatus buildResponseStatusMock(int code, String reason, String body) throws IcException {
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
    final int STATUS_CODE = 400;
    final String REASON_STRING = "reason";
    final String BODY_STRING = "body string";

    IResponseStatus status = this.buildResponseStatusMock(STATUS_CODE, REASON_STRING, BODY_STRING);

    StatusCodeValidator statusCodeValidator = new StatusCodeValidator(status);

    IcException exception =
        Assertions.assertThrows(IcException.class, statusCodeValidator::validateAndTryThrowException);
   

    String exceptionMessage = exception.getMessage();
    Assertions.assertTrue(exceptionMessage.contains("" + STATUS_CODE));
    Assertions.assertTrue(exceptionMessage.contains(REASON_STRING));
    Assertions.assertTrue(exceptionMessage.contains(BODY_STRING));
  }

}
