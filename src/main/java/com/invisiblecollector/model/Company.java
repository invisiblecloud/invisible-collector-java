package com.invisiblecollector.model;

/**
 * A model for the company.
 *
 * @author ros
 */
public class Company extends Model implements IRoutable {

  @Override
  public int hashCode() {
    pmdWorkaround();
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Company)) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    Company other = (Company) obj;
    return super.equals(other);
  }

  public String getAddress() {
    return getString("address");
  }

  public String getCity() {
    return getString("city");
  }

  public String getCountry() {
    return getString("country");
  }

  public String getId() {
    return getString("gid");
  }

  public String getName() {
    return getString("name");
  }

  @Override
  public String getRoutableId() {
    return getId();
  }

  public String getVatNumber() {
    return getString("vatNumber");
  }

  public String getZipCode() {
    return getString("zipCode");
  }

  public Boolean isNotificationsEnabled() {
    return getBoolean("notificationsEnabled");
  }

  public void setAddress(String address) {
    fields.put("address", address);
  }

  public void setCity(String city) {
    fields.put("city", city);
  }

  /**
   * Set the company's country
   *
   * @param country The company's country. Value must be in <a href="https://en.wikipedia.org/wiki/ISO_3166-1">ISO 3166-1</a> format.
   */
  public void setCountry(String country) {
    assertCountryIso3166(country);

    fields.put("country", country);
  }

  public void setGid(String id) {
    fields.put("gid", id);
  }

  public void setName(String name) {
    fields.put("name", name);
  }

  /**
   * Set the vat number
   *
   * @param vatNumber the VAT number. This number is validated for correctness
   */
  public void setVatNumber(String vatNumber) {
    fields.put("vatNumber", vatNumber);
  }

  public void setZipCode(String zipCode) {
    fields.put("zipCode", zipCode);
  }

  public void setNotificationsEnabled(Boolean notificationsEnabled) {
    fields.put("notificationsEnabled", notificationsEnabled);
  }
}
