package com.ic.invoicecapture.response.validators;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.connection.response.validators.StatusCodeValidator;
import com.ic.invoicecapture.exceptions.IcException;

public class StatusCodeValidatorTest {


  private StatusLine statusLineMock;
  private ServerResponse serverResponseMock;


  @BeforeEach
  public void init() {
    this.statusLineMock = EasyMock.createNiceMock(StatusLine.class);
    EasyMock.createNiceMock(HttpEntity.class);
    this.serverResponseMock = EasyMock.createNiceMock(ServerResponse.class);
  }


  @Test
  public void validate_pass() throws IcException {
    EasyMock.expect(this.serverResponseMock.getStatusLine()).andReturn(this.statusLineMock);
    EasyMock.expect(this.statusLineMock.getStatusCode()).andReturn(200);
    EasyMock.replay(this.serverResponseMock);
    EasyMock.replay(this.statusLineMock);

    StatusCodeValidator statusCodeValidator = new StatusCodeValidator(this.serverResponseMock);

    statusCodeValidator.validateAndTryThrowException();

    EasyMock.verify(this.statusLineMock);
    EasyMock.verify(this.serverResponseMock);
  }

  @Test
  public void validate_fail() throws IcException {
    final String BODY_STRING = "body string";
    final int STATUS_CODE = 400;

    EasyMock.expect(this.serverResponseMock.getStatusLine()).andReturn(this.statusLineMock);
    EasyMock.expect(this.statusLineMock.getStatusCode()).andReturn(STATUS_CODE);
    EasyMock.expect(this.serverResponseMock.getBodyAsString()).andReturn(BODY_STRING);
    EasyMock.replay(this.serverResponseMock);
    EasyMock.replay(this.statusLineMock);

    StatusCodeValidator statusCodeValidator = new StatusCodeValidator(this.serverResponseMock);

    IcException exception =
        Assertions.assertThrows(IcException.class, statusCodeValidator::validateAndTryThrowException);
    EasyMock.verify(this.statusLineMock);
    EasyMock.verify(this.serverResponseMock);

    String exceptionMessage = exception.getMessage();
    Assertions.assertTrue(exceptionMessage.contains("" + STATUS_CODE));
    Assertions.assertTrue(exceptionMessage.contains(BODY_STRING));

  }

}
