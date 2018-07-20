package com.ic.invoicecapture.model;

import java.util.Map;

public enum CompanyField implements ICheckableField {
  ADDRESS("address"), CITY("city"), NAME("name"), VAT_NUMBER("vatNumber"), ZIP_CODE(
      "zipCode"), COUNTRY("country");

  public static void assertCorrectlyInitialized(Map<CompanyField, Object> companyInfo)
      throws IllegalArgumentException {
    FieldEnumUtils.assertCorrectlyInitializedEnumMap(companyInfo, "Company", CompanyField.NAME,
        CompanyField.VAT_NUMBER);
  }

  private final String jsonName;

  private CompanyField(String jsonName) {
    this.jsonName = jsonName;
  }

  @Override
  public boolean isValidValue(Object value) {
    return value == null || value instanceof String;
  }

  @Override
  public String toString() {
    return this.jsonName;
  }

}
