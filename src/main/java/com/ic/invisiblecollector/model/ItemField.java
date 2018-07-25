package com.ic.invisiblecollector.model;

import java.util.Map;

/**
 * The attributes which can be used to set a debt's item
 * information in the database.
 * 
 * <p>Intended to be used in conjunction with an enum map.
 * 
 * <p>See also {@link Item} for a ready 'container' of these attributes, 
 * and {@link DebtField#ITEMS} for the 'containing' attribute.
 * 
 * @see Debt
 * @author ros
 */
public enum ItemField implements ICheckableField {
  /** The Item's description. 
   * Expected value is a {@link String} */
  DESCRIPTION("description", FieldEnumUtils::assertStringObject),
  /** The Item's name. 
   * Expected value is a {@link String} */
  NAME("name", FieldEnumUtils::assertStringObject),
  /** 
   * The Item's price (gross total).
   * Expected value is a 'number' ({@link Integer}, {@link Float} or {@link Double}).
   * Default value is 0.0
   */
  PRICE("price", FieldEnumUtils::assertNumberObject),
  /** 
   * The amount of items of this type included in the transaction.
   * Expected value is a 'number' ({@link Integer}, {@link Float} or {@link Double}).
   * Default value is 0.0
   */
  QUANTITY("quantity", FieldEnumUtils::assertNumberObject), 
  /** 
   * The Item's vat (percentage).
   * Expected value is a 'number' ({@link Integer}, {@link Float} or {@link Double}).
   * Default value is 0.0
   */
  VAT("vat", FieldEnumUtils::assertNumberObject);

  /**
   * Validate the attribute map. 
   * 
   * <p>Mandatory attributes are {@link #NAME }.
   * 
   * @param itemInfo the item attributes map
   * @throws IllegalArgumentException which is thrown in case the {@code itemInfo} 
   *         map is invalid
   */
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