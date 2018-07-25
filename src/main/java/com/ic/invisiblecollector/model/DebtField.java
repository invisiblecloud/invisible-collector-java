package com.ic.invisiblecollector.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The attributes which can be used to update a debt's (related to a customer),
 * information in the database.
 * 
 * <p>Intended to be used in conjunction with an enum map.
 * 
 * <p>See also {@link Debt} for a ready 'container' of these attributes.
 * 
 * @author ros
 */
public enum DebtField implements ICheckableField {
  /** 
   * The Debt's attributes.
   * Expected value is a {@link Map} with {@link String} 
   * keys and values ({@code Map<String, String>}). 
   */
  ATTRIBUTES("attributes", FieldEnumUtils::assertStringMapObject), 
  /** 
   * The Debt's currency. 
   * Expected value is a {@link String} 
   * Value must be in <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217</a> format.
   */
  CURRENCY("currency", FieldEnumUtils::assertStringObject), 
  /** The id of the customer to whom the debt is issued. Can be the customer's id or external id.
   * Expected value is a {@link String} */
  CUSTOMER_ID("customerId", FieldEnumUtils::assertStringObject),
  /** 
   * The date when the debt was issued. 
   * Expected value is a {@link java.util.Date}.
   * 
   * <p>Only the year, month and day are considered, with the remaining fields discarded.
   */
  DATE("date", FieldEnumUtils::assertDateObject), 
  /** The date when the debt is due.
   *  Expected value is a {@link java.util.Date}.
   *  
   *  <p>Only the year, month and day are considered, with the remaining fields discarded.
   */
  DUE_DATE("dueDate", FieldEnumUtils::assertDateObject), 
  /** The gross total.
   * Expected value is a 'number' ({@link Integer}, {@link Float} or {@link Double}) */
  GROSS_TOTAL("grossTotal", FieldEnumUtils::assertNumberObject), 
  /** 
   * The debt items.
   * Expected value is an array ({@code Object[]}) or {@link List} 
   * of maps ({@link Map}) of item attributes. 
   * See {@link ItemField} for the possible map attributes or use {@link Item} to 
   * create the maps.
   */
  ITEMS("items", DebtField::assertListOfItemMaps), 
  /** The net total.
   * Expected value is a 'number' ({@link Integer}, {@link Float} or {@link Double}) */
  NET_TOTAL("netTotal", FieldEnumUtils::assertNumberObject), 
  /** The debt number.
   * Expected value is a {@link String} */
  NUMBER("number", FieldEnumUtils::assertStringObject), 
  /** The debt status.
   * Expected value is a {@link String}. 
   * 
   * <p>Value must be one of: <br>
   * "PENDING" - the default value; <br>
   * "PAID"; <br>
   * "CANCELLED"; <br>
   * 
   * <p>Check <a href="https://www.invisiblecollector.com/docs/api/debts/post/">the API docs </a> for up to date acceptable values
   */
  STATUS("status", FieldEnumUtils::assertStringObject), 
  /** The total tax amount.
   * Expected value is a 'number' ({@link Integer}, {@link Float} or {@link Double}) */
  TAX("tax", FieldEnumUtils::assertNumberObject), 
  /** The debt type.
   * Expected value is a {@link String}
   * 
   * <p>Value must be one of: <br> 
   * "FT" - Normal invoice; <br>
   * "FS" - Simplified invoice; <br> 
   * "SD" - Standard debt; <br>
   * 
   * <p>Check <a href="https://www.invisiblecollector.com/docs/api/debts/post/">the API docs </a> for up to date acceptable values
   */
  TYPE("type", FieldEnumUtils::assertStringObject);

  /**
   * Validate the attribute map. 
   * 
   * <p>Mandatory attributes are {@link #NUMBER }, {@link #CUSTOMER_ID }, 
   * {@link #TYPE }, {@link #DATE } and {@link #DUE_DATE }.
   * 
   * @param debtInfo the debt attributes map
   * @throws IllegalArgumentException which is thrown in case the {@code debtInfo} 
   *         map is invalid
   */
  public static void assertCorrectlyInitialized(Map<DebtField, Object> debtInfo)
      throws IllegalArgumentException {
    FieldEnumUtils.assertCorrectlyInitializedEnumMap(debtInfo, "Debt", DebtField.NUMBER,
        DebtField.CUSTOMER_ID, DebtField.TYPE, DebtField.DATE, DebtField.DUE_DATE);
  }

  @SuppressWarnings("unchecked")
  private static void assertListOfItemMaps(Object value) {
    if (value == null) {
      return;
    } else if (value instanceof List || value instanceof Object[]) {
      List<Object> items =
          value instanceof Object[] ? Arrays.asList((Object[]) value) : (List<Object>) value;

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
      throw new IllegalArgumentException("items must be a List or Array type");
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
