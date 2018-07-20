package com.ic.invoicecapture.model;

import java.util.Map;

public enum CustomerField implements ICheckableField {
  ADDRESS("address"), CITY("city"), EXTERNAL_ID("externalId"), NAME("name"), VAT_NUMBER(
      "vatNumber"), ZIP_CODE("zipCode"), COUNTRY("country"), EMAIL("email"), PHONE("phone");

  public static void assertCorrectlyInitialized(Map<CustomerField, Object> customerInfo)
      throws IllegalArgumentException {
    FieldEnumUtils.assertCorrectlyInitializedEnumMap(customerInfo, "Customer", CustomerField.NAME,
        CustomerField.VAT_NUMBER);
  }

  private final String jsonName;

  private CustomerField(String jsonName) {
    this.jsonName = jsonName;
  }

  @Override
  public boolean isValidValue(Object value) {
    return FieldEnumUtils.isStringObject(value);
  }

  @Override
  public String toString() {
    return this.jsonName;
  }
}
