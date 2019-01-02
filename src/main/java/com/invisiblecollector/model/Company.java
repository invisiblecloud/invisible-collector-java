package com.invisiblecollector.model;

/**
 * A model for the company.
 *
 * <p>Can be converted into an enum map, see {@link #toEnumMap()} and {@link CompanyField} for more
 * details.
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

  public void setCountry(String country) {
    fields.put("country", country);
  }

  public void setGid(String id) {
    fields.put("gid", id);
  }

  public void setName(String name) {
    fields.put("name", name);
  }

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
