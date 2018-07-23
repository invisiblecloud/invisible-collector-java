package com.ic.invoicecapture.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DebtFieldTest {

  @Test
  public void assertValueIsValid_passFloatingPoint() {
    DebtField.NET_TOTAL.assertValueIsValid(0.4f);
    DebtField.NET_TOTAL.assertValueIsValid(0.4d);
    DebtField.NET_TOTAL.assertValueIsValid(13);
  }

  @Test
  public void assertValueIsValid_failFloatingPoint() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> DebtField.NET_TOTAL.assertValueIsValid("asd"));
  }

  @Test 
  public void assertValueIsValid_passStringMap() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> DebtField.NET_TOTAL.assertValueIsValid("asd"));
  }
}
