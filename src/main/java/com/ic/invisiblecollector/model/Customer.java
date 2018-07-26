package com.ic.invisiblecollector.model;

import java.util.EnumMap;
import java.util.Objects;

/**
 * A model for the customer. 
 * 
 * <p>Can be converted into an enum map, 
 * see {@link #toEnumMap()} and {@link CustomerField} for more details.
 * 
 * @author ros
 */
public class Customer implements IModel, IRoutable {
  
  private String address;
  private String city;
  private String country;
  private String email;
  private String externalId;
  private String gid;
  private String name;
  private String phone;
  private String vatNumber;
  private String zipCode;
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Customer)) {
      return false;
    } else if (this == obj) {
      return true;
    } else {
      Customer other = (Customer) obj;
      return Objects.equals(this.gid, other.gid)
          && Objects.equals(this.vatNumber, other.vatNumber)
          && Objects.equals(this.name, other.name) && Objects.equals(this.address, other.address)
          && Objects.equals(this.zipCode, other.zipCode) && Objects.equals(this.city, other.city)
          && Objects.equals(this.country, other.country) && Objects.equals(this.email, other.email)
          && Objects.equals(this.externalId, other.externalId)
          && Objects.equals(this.phone, other.phone);
    }
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  /**
   * See {@link CustomerField#COUNTRY} for more details.
   */
  public String getCountry() {
    return country;
  }

  public String getEmail() {
    return email;
  }

  /**
   * See {@link #setExternalId(String)} for more details.
   */
  public String getExternalId() {
    return externalId;
  }

  /**
   * See {@link #setId(String)} for more details.
   */
  public String getId() {
    return gid;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }

  @Override
  public String getRoutableId() {
    String gid = getId();
    String externalId = getExternalId();
    if (gid != null && !gid.isEmpty()) {
      return gid;
    } else if (externalId != null && !externalId.isEmpty()) {
      return externalId;
    } else {
      throw new IllegalArgumentException("no valid id contained in object");
    }
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
        this.country, this.gid, this.email, this.externalId, this.phone);
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setCity(String city) {
    this.city = city;
  }

  /**
   * See {@link CustomerField#COUNTRY} for more details.
   */
  public void setCountry(String country) {
    this.country = country;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Set the external id of the model. The external id can be for example the id of the 
   * corresponding model in the local database.
   * 
   * @param externalId the external id
   */
  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  /**
   * The id of the model in the external database (as returned by any request).
   * 
   * @param id the id.
   */
  public void setId(String id) {
    this.gid = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setVatNumber(String vatNumber) {
    this.vatNumber = vatNumber;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @Override
  public EnumMap<CustomerField, Object> toEnumMap() {
    EnumMap<CustomerField, Object> map = new EnumMap<>(CustomerField.class);

    ModelUtils.tryAddObject(map, CustomerField.NAME, getName());
    ModelUtils.tryAddObject(map, CustomerField.ADDRESS, getAddress());
    ModelUtils.tryAddObject(map, CustomerField.VAT_NUMBER, getVatNumber());
    ModelUtils.tryAddObject(map, CustomerField.ZIP_CODE, getZipCode());
    ModelUtils.tryAddObject(map, CustomerField.CITY, getCity());
    ModelUtils.tryAddObject(map, CustomerField.EXTERNAL_ID, getExternalId());
    ModelUtils.tryAddObject(map, CustomerField.COUNTRY, getCountry());
    ModelUtils.tryAddObject(map, CustomerField.EMAIL, getEmail());
    ModelUtils.tryAddObject(map, CustomerField.PHONE, getPhone());
    
    return map;
  }
}
