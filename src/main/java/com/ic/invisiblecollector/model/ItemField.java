package com.ic.invisiblecollector.model;

import java.util.Map;

public enum ItemField implements ICheckableField {
  DESCRIPTION("description", FieldEnumUtils::assertStringObject), 
  NAME("name", FieldEnumUtils::assertStringObject), 
  PRICE("price", FieldEnumUtils::assertNumberObject), 
  QUANTITY("quantity", FieldEnumUtils::assertNumberObject), 
  VAT("vat", FieldEnumUtils::assertNumberObject);

  public static void assertCorrectlyInitialized(Map<ItemField, Object> itemInfo)
      throws IllegalArgumentException {
    FieldEnumUtils.assertCorrectlyInitializedEnumMap(itemInfo, "Item", ItemField.NAME);
  }

  private final String jsonName;
  private final ICheckableField validator;

  private ItemField(String jsonName, ICheckableField validator) {
    this.jsonName = jsonName;
    this.validator = validator;
  }

  @Override
  public void assertValueIsValid(Object value) throws IllegalArgumentException {
    validator.assertValueIsValid(value);
  }

  @Override
  public String toString() {
    return this.jsonName;
  }
}