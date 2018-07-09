package com.ic.invoicecapture.response.validators;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.connection.response.validators.StatusCodeValidator;
import com.ic.invoicecapture.connection.response.validators.ValidationResult;
import com.ic.invoicecapture.exceptions.IcException;

public class StatusCodeValidatorTest extends ValidationBase {


  private StatusLine statusLineMock;
  private ServerResponse serverResponseMock;


  @Before
  public void init() {
    this.statusLineMock = EasyMock.createNiceMock(StatusLine.class);
    EasyMock.createNiceMock(HttpEntity.class);
    this.serverResponseMock = EasyMock.createNiceMock(ServerResponse.class);
  }


  @Test
  public void validate_pass() {
    EasyMock.expect(this.serverResponseMock.getStatusLine()).andReturn(this.statusLineMock);
    EasyMock.expect(this.statusLineMock.getStatusCode()).andReturn(200);
    EasyMock.replay(this.serverResponseMock);
    EasyMock.replay(this.statusLineMock);

    StatusCodeValidator statusCodeValidator = new StatusCodeValidator(this.serverResponseMock);

    ValidationResult validationResult = statusCodeValidator.validate();

    this.assertValid(validationResult);
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

    StatusCodeValidator jsonValidator = new StatusCodeValidator(this.serverResponseMock);

    ValidationResult validationResult = jsonValidator.validate();

    IcException exception = validationResult.getException();
    this.assertNotValid(validationResult);
    EasyMock.verify(this.statusLineMock);
    EasyMock.verify(this.serverResponseMock);
    
    String exceptionMessage = exception.getMessage();
    Assert.assertTrue(exceptionMessage.contains("" + STATUS_CODE));
    Assert.assertTrue(exceptionMessage.contains(BODY_STRING));
    
  }

}
