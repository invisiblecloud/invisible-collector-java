package com.invisiblecollector.model;

import java.util.EnumMap;
import java.util.Objects;

/**
 * A model for the company. 
 * 
 * <p>Can be converted into an enum map, 
 * see {@link #toEnumMap()} and {@link CompanyField} for more details.
 * 
 * @author ros
 *
 */
public class Company extends Model implements IModel, IRoutable {

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Company)) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    Company other = (Company) obj;
    return Objects.equals(this.getVatNumber(), other.getVatNumber())
        && Objects.equals(this.getName(), other.getName())
        && Objects.equals(this.getAddress(), other.getAddress())
        && Objects.equals(this.getZipCode(), other.getZipCode())
        && Objects.equals(this.getCity(), other.getCity())
        && Objects.equals(this.getCountry(), other.getCountry())
        && Objects.equals(this.getId(), other.getId())
        && Objects.equals(this.isNotificationsEnabled(), other.isNotificationsEnabled());
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
  
  @Override
  public int hashCode() {
    return Objects.hash(this.getVatNumber(), this.getName(), this.getAddress(), this.getZipCode(), this.getCity(),
        this.getCountry(), this.getId(), this.isNotificationsEnabled());
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
  
  public void setId(String id) {
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

  @Override
  public EnumMap<CompanyField, Object> toEnumMap() {
    EnumMap<CompanyField, Object> map = new EnumMap<>(CompanyField.class);

    ModelUtils.tryAddObject(map, CompanyField.NAME, getName());
    ModelUtils.tryAddObject(map, CompanyField.ADDRESS, getAddress());
    ModelUtils.tryAddObject(map, CompanyField.VAT_NUMBER, getVatNumber());
    ModelUtils.tryAddObject(map, CompanyField.ZIP_CODE, getZipCode());
    ModelUtils.tryAddObject(map, CompanyField.CITY, getCity());
    ModelUtils.tryAddObject(map, CompanyField.COUNTRY, getCountry());
    
    return map;
  }
}
