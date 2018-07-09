package com.ic.invoicecapture.response.validators;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.easymock.EasyMock;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.ic.invoicecapture.connection.response.ServerResponse;
import com.ic.invoicecapture.connection.response.validators.StatusCodeValidator;
import com.ic.invoicecapture.connection.response.validators.ValidationResult;
import com.ic.invoicecapture.exceptions.IcException;

public class StatusCodeValidatorTest {


  private StatusLine statusLineMock;
  private HttpEntity bodyEntity;
  private ServerResponse serverResponseMock;


  @Before
  public void init() {
    this.statusLineMock = EasyMock.createNiceMock(StatusLine.class);
    this.bodyEntity = EasyMock.createNiceMock(HttpEntity.class);
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

    Assert.assertEquals(validationResult.isValid(), true);
    Assert.assertEquals(validationResult.getException(), null);
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
    Assert.assertEquals(validationResult.isValid(), false);
    Assert.assertNotEquals(exception, null);
    EasyMock.verify(this.statusLineMock);
    EasyMock.verify(this.serverResponseMock);
    
    String exceptionMessage = exception.getMessage();
    Assert.assertTrue(exceptionMessage.contains("" + STATUS_CODE));
    Assert.assertTrue(exceptionMessage.contains(BODY_STRING));
    
  }

}
