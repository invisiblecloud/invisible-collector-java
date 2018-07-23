package com.ic.invoicecapture.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public enum DebtField implements ICheckableField {

  ATTRIBUTES("attributes", FieldEnumUtils::assertStringMapObject), CURRENCY("currency",
      FieldEnumUtils::assertStringObject), CUSTOMER_ID("customerId",
          FieldEnumUtils::assertStringObject), DATE("date",
              FieldEnumUtils::assertDateObject), DUE_DATE("dueDate",
                  FieldEnumUtils::assertDateObject), GROSS_TOTAL("grossTotal",
                      FieldEnumUtils::assertNumberObject), ITEMS("items",
                          DebtField::assertListOfItemMaps), NET_TOTAL("netTotal",
                              FieldEnumUtils::assertNumberObject), NUMBER("number",
                                  FieldEnumUtils::assertStringObject), STATUS("status",
                                      FieldEnumUtils::assertStringObject), TAX("tax",
                                          FieldEnumUtils::assertNumberObject), TYPE("type",
                                              FieldEnumUtils::assertStringObject);

  public enum ItemField implements ICheckableField {
    DESCRIPTION("description", FieldEnumUtils::assertStringObject), NAME("name",
        FieldEnumUtils::assertStringObject), PRICE("price",
            FieldEnumUtils::assertNumberObject), QUANTITY("quantity",
                FieldEnumUtils::assertNumberObject), VAT("vat", FieldEnumUtils::assertNumberObject);


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

  public static void assertCorrectlyInitialized(Map<DebtField, Object> debtInfo)
      throws IllegalArgumentException {
    FieldEnumUtils.assertCorrectlyInitializedEnumMap(debtInfo, "Debt", DebtField.NUMBER,
        DebtField.CUSTOMER_ID, DebtField.TYPE, DebtField.DATE, DebtField.DUE_DATE);
  }

  @SuppressWarnings("unchecked")
  private static void assertListOfItemMaps(Object value) {
    if (value == null) {
      return;
    } else if (value instanceof List) {
      List<Object> items = (List<Object>) value;
      for (Object item : items) {
        if (!(item instanceof Map)) {
          throw new IllegalArgumentException("Item must be of type Map");
        } else {
          Map<Object, Object> itemMap = (Map<Object, Object>) item;
          for (Entry<Object, Object> entry : itemMap.entrySet()) {
            Object key = entry.getKey();
            if (!(key instanceof ItemField)) { // check if all keys of type ItemField
              String msg = String.format("item map key (type: %s) must be of type ItemField",
                  key.getClass());
              throw new IllegalArgumentException(msg);
            } else { // check if value is valid
              ItemField field = (ItemField) key;
              field.assertValueIsValid(entry.getValue());
            }
          }
        }
      }
    } else {
      throw new IllegalArgumentException("items must be a List type");
    }
  }

  private final String jsonName;

  private final ICheckableField validator;

  private DebtField(String jsonName, ICheckableField validator) {
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
