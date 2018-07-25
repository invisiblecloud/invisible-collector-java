package com.ic.invisiblecollector.connection.response.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.ic.invisiblecollector.connection.response.IServerResponse;
import com.ic.invisiblecollector.connection.response.validators.GeneralStatusCodeValidator;
import com.ic.invisiblecollector.exceptions.IcException;

public class GeneralStatusCodeValidatorTest {

  protected IServerResponse buildResponseStatusMock(int code, String reason, String body)
      throws IcException {
    return new ServerResponseMockBuilder().setStatusCode(code).setReason(reason).setBody(body)
        .build();
  }

  @Test
  public void validate_success() throws IcException {
    IServerResponse status = this.buildResponseStatusMock(200, "OK", "body");

    new GeneralStatusCodeValidator().assertValidResponse(status);
  }

  @Test
  public void validate_fail() throws IcException {
    final int statusCode = 400;
    final String reasonMsg = "reason";
    final String boyMsg = "body string";

    IServerResponse status = this.buildResponseStatusMock(statusCode, reasonMsg, boyMsg);

    GeneralStatusCodeValidator statusCodeValidator = new GeneralStatusCodeValidator();

    IcException exception = Assertions.assertThrows(IcException.class,
        () -> statusCodeValidator.assertValidResponse(status));


    String exceptionMessage = exception.getMessage();
    Assertions.assertTrue(exceptionMessage.contains("" + statusCode));
    Assertions.assertTrue(exceptionMessage.contains(reasonMsg));
    Assertions.assertTrue(exceptionMessage.contains(boyMsg));
  }

}
