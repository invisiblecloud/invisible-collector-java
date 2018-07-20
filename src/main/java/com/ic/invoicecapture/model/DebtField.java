package com.ic.invoicecapture.model;

import java.util.Map;

public enum DebtField implements ICheckableField {
  NUMBER("number"), CUSTOMER_ID("customerId"), TYPE("type"), STATUS("status"), DATE("date") {
    @Override
    public boolean isValidValue(Object value) {
      return FieldEnumUtils.isDateObject(value);
    }
  },
  DUE_DATE("dueDate") {
    @Override
    public boolean isValidValue(Object value) {
      return FieldEnumUtils.isDateObject(value);
    }
  },
  NET_TOTAL("netTotal") {
    @Override
    public boolean isValidValue(Object value) {
      return FieldEnumUtils.isFloatingPointObject(value);
    }
  },
  TAX("tax") {
    @Override
    public boolean isValidValue(Object value) {
      return FieldEnumUtils.isFloatingPointObject(value);
    }
  },
  GROSS_TOTAL("grossTotal") {
    @Override
    public boolean isValidValue(Object value) {
      return FieldEnumUtils.isFloatingPointObject(value);
    }
  },
  CURRENCY("currency");
  // TODO add attributes and items

  public static void assertCorrectlyInitialized(Map<DebtField, Object> debtInfo)
      throws IllegalArgumentException {
    FieldEnumUtils.assertCorrectlyInitializedEnumMap(debtInfo, "Debt", DebtField.NUMBER,
        DebtField.CUSTOMER_ID, DebtField.TYPE, DebtField.DATE, DebtField.DUE_DATE);
  }

  private final String jsonName;

  private DebtField(String jsonName) {
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
