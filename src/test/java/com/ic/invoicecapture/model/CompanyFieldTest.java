package com.ic.invoicecapture.model;

import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompanyFieldTest {
  @Test
  public void isValidValue_nullPass() {
    Assertions.assertTrue(CompanyField.ADDRESS.isValidValue(null));
  }

  @Test
  public void isValidValue_stringPass() {
    Assertions.assertTrue(CompanyField.ADDRESS.isValidValue("hi"));
  }

  @Test
  public void isValidValue_diferentTypeFail() {
    Integer integer = 123;
    Assertions.assertFalse(CompanyField.ADDRESS.isValidValue(integer));
  }

  private Map<CompanyField, Object> buildMap() {
    Map<CompanyField, Object> companyInfo = new EnumMap<>(CompanyField.class);
    companyInfo.put(CompanyField.ADDRESS, "new address");

    return companyInfo;
  }

  @Test
  public void assertCorrectlyInitialized_missingMandatoryFields() {
    Map<CompanyField, Object> companyInfo = buildMap();
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> CompanyField.assertCorrectlyInitialized(companyInfo));
  }
  
  @Test
  public void assertCorrectlyInitialized_invalidType() {
    Map<CompanyField, Object> companyInfo = buildMap();
    companyInfo.put(CompanyField.NAME, (Integer) 23432);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> CompanyField.assertCorrectlyInitialized(companyInfo));
  }

}
