package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.connection.response.validators.ValidatorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidatorBuilderTest {
  
  @Test
  public void build_get() {
    ValidatorBuilder builder = new ValidatorBuilder();
    Assertions.assertNotNull(builder.build());
  }
}
