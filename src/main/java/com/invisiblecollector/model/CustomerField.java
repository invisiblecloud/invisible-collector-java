package com.invisiblecollector.model;

import java.util.Map;

/**
 * The attributes which can be used to update a customer's information in the database.
 * 
 * <p>Intended to be used in conjunction with an enum map.
 * 
 * <p>See also {@link Customer} for a ready 'container' of these attributes.
 * 
 * @author ros
 */
public enum CustomerField implements ICheckableField {
  /** The customer's address.
   * Expected values is a {@link String} */
  ADDRESS("address"), 
  /** The customer's city name.
   * Expected values is a {@link String} */
  CITY("city"), 
  /** The customer's external id. This can be any id such as the corresponding
   * model's internal id.
   * Expected values is a {@link String} */
  EXTERNAL_ID("externalId"), 
  /** The customer's name.
   * Expected values is a {@link String} */
  NAME("name"), 
  /** The customer's VAT Number.
   * Expected values is a {@link String} */
  VAT_NUMBER("vatNumber"), 
  /** The customer's zip code.
   * Expected values is a {@link String} */
  ZIP_CODE("zipCode"), 
  /** 
   * The customer's country.
   * Expected values is a {@link String}.
   * Value must be in <a href="https://en.wikipedia.org/wiki/ISO_3166-1">ISO 3166-1</a> format. 
   */
  COUNTRY("country"), 
  /** The customer's email.
   * Expected values is a {@link String} */
  EMAIL("email"), 
  /** The customer's phone.
   * Expected values is a {@link String} */
  PHONE("phone");

  /**
   * Validate the attribute map. 
   * 
   * <p>Mandatory attributes are {@link #NAME } and {@link #VAT_NUMBER}.
   * 
   * @param customerInfo the customer attributes map
   * @throws IllegalArgumentException which is thrown in case the {@code customerInfo} 
   *         map is invalid
   */
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
  public void assertValueIsValid(Object value) throws IllegalArgumentException {
    FieldEnumUtils.assertStringObject(value);
  }

  @Override
  public String toString() {
    return this.jsonName;
  }
}
