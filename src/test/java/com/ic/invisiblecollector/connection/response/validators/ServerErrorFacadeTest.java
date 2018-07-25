package com.ic.invisiblecollector.connection.response.validators;

import com.ic.invisiblecollector.connection.response.validators.ServerErrorFacade;
import com.ic.invisiblecollector.exceptions.IcException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServerErrorFacadeTest {

  private static final String JSON_KEY = "message";
  private static final String JSON_VALUE = "test-value";
  private static final String TEST_JSON = String.format("{ \"%s\":\"%s\" }", JSON_KEY, JSON_VALUE);

  @Test
  public void constructor_fail() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new ServerErrorFacade("bad/ json"));
  }

  @Test
  public void getErrorMember_success() throws IcException {
    ServerErrorFacade serverErrorFacade = new ServerErrorFacade(TEST_JSON);
    String value = serverErrorFacade.getErrorMember(JSON_KEY);
    Assertions.assertEquals(JSON_VALUE, value);
  }

  @Test
  public void getErrorMember_failMissingKey() throws IcException {
    ServerErrorFacade serverErrorFacade = new ServerErrorFacade(TEST_JSON);
    Assertions.assertThrows(IcException.class,
        () -> serverErrorFacade.getErrorMember("new-header"));
  }
  
  @Test
  public void getErrorMessage_success() throws IcException {
    ServerErrorFacade serverErrorFacade = new ServerErrorFacade(TEST_JSON);
    String value = serverErrorFacade.getErrorMessage();
    Assertions.assertEquals(JSON_VALUE, value);
  }
  
  @Test
  public void getErrorMessage_failMissingKey() throws IcException {
    ServerErrorFacade serverErrorFacade = new ServerErrorFacade("{}");
    Assertions.assertThrows(IcException.class,
        () -> serverErrorFacade.getErrorMessage());
  }
}
