package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.RequestType;
import com.ic.invoicecapture.connection.response.ServerResponseFacade;
import com.ic.invoicecapture.connection.response.validators.ValidatorFactory;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidatorFactoryTest {
  
  @Test
  public void build_get() {
    ValidatorFactory factory = new ValidatorFactory();
    ServerResponseFacade responsePair = EasyMock.createNiceMock(ServerResponseFacade.class);
    Assertions.assertNotEquals(null, factory.build(RequestType.GET, responsePair));
  }
}
