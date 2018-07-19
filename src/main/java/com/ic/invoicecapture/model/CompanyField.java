package com.ic.invoicecapture.model;

import java.util.Map;

public enum CompanyField implements ICheckableField {
  NAME("name"), VAT_NUMBER("vatNumber"), ADDRESS("address"), ZIP_CODE("zipCode"), CITY("city");

  private final String jsonName;

  private CompanyField(String jsonName) {
    this.jsonName = jsonName;
  }

  @Override
  public String toString() {
    return this.jsonName;
  }

  public static void assertCorrectlyInitialized(Map<CompanyField, Object> companyInfo)
      throws IllegalArgumentException {
    FieldEnumUtils.assertCorrectlyInitializedEnumMap(companyInfo, "Company", CompanyField.NAME,
        CompanyField.VAT_NUMBER);
  }

  @Override
  public boolean isValidValue(Object value) {
    return value == null || value instanceof String;
  }

}
