package com.invisiblecollector.model;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompanyFieldTest {
  @Test
  public void isValidValue_nullPass() {
    CompanyField.ADDRESS.assertValueIsValid(null);
  }

  @Test
  public void isValidValue_stringPass() {
    CompanyField.ADDRESS.assertValueIsValid("hi");
  }

  @Test
  public void isValidValue_diferentTypeFail() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> CompanyField.ADDRESS.assertValueIsValid(123));
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
