package com.invisiblecollector.model;

import java.util.Map;

/**
 * The attributes which can be used to update a company's information in the database.
 * 
 * <p>Intended to be used in conjunction with an enum map.
 * 
 * <p>See also {@link Company} for a ready 'container' of these attributes.
 * 
 * @author ros
 */
public enum CompanyField implements ICheckableField {
  /** The company's address. 
   * Expected value is a {@link String}.*/
  ADDRESS("address"), 
  /** The company's city name. 
   * Expected value is a {@link String}.*/
  CITY("city"), 
  /** The company's name.
   * Expected value is a {@link String}.*/
  NAME("name"), 
  /** The company's VAT Number.
   * Expected value is a {@link String}.*/
  VAT_NUMBER("vatNumber"), 
  /** The company's zip code.
   * Expected value is a {@link String}.*/
  ZIP_CODE("zipCode"), 
  /** 
   * The company's country.
   * Expected value is a {@link String}.
   * Value must be in <a href="https://en.wikipedia.org/wiki/ISO_3166-1">ISO 3166-1</a> format.
   */
  COUNTRY("country");

  /**
   * Validate the attribute map. 
   * 
   * <p>Mandatory attributes are {@link #NAME} and {@link #VAT_NUMBER}
   * 
   * @param companyInfo the company attributes map
   * @throws IllegalArgumentException which is thrown in case the {@code companyInfo} map is invalid
   */
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
  public void assertValueIsValid(Object value) throws IllegalArgumentException {
    FieldEnumUtils.assertStringObject(value);
  }

  @Override
  public String toString() {
    return this.jsonName;
  }

}
