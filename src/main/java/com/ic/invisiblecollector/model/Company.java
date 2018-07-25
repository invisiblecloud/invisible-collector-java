package com.ic.invisiblecollector.model;

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
public class Company implements IModel, IRoutable {

  private String address;
  private String city;
  private String country;
  private String gid;
  private String name;
  private Boolean notificationsEnabled;
  private String vatNumber;
  private String zipCode;
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Company)) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    Company other = (Company) obj;
    return Objects.equals(this.vatNumber, other.vatNumber)
        && Objects.equals(this.name, other.name)
        && Objects.equals(this.address, other.address)
        && Objects.equals(this.zipCode, other.zipCode) 
        && Objects.equals(this.city, other.city)
        && Objects.equals(this.country, other.country) 
        && Objects.equals(this.gid, other.gid)
        && Objects.equals(this.notificationsEnabled, other.notificationsEnabled);
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getId() {
    return gid;
  }

  public String getName() {
    return name;
  }

  public String getVatNumber() {
    return vatNumber;
  }

  public String getZipCode() {
    return zipCode;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.vatNumber, this.name, this.address, this.zipCode, this.city,
        this.country, this.gid, this.notificationsEnabled);
  }
  
  public Boolean isNotificationsEnabled() {
    return notificationsEnabled;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVatNumber(String vatNumber) {
    this.vatNumber = vatNumber;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
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
