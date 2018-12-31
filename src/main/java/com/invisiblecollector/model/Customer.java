package com.invisiblecollector.model;

import java.util.EnumMap;
import java.util.Objects;

/**
 * A model for the customer.
 *
 * <p>Can be converted into an enum map, see {@link #toEnumMap()} and {@link CustomerField} for more
 * details.
 *
 * @author ros
 */
public class Customer extends Model implements IModel, IRoutable {

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Customer)) {
      return false;
    } else if (this == obj) {
      return true;
    } else {
      Customer other = (Customer) obj;
      return Objects.equals(this.getId(), other.getId())
          && Objects.equals(this.getVatNumber(), other.getVatNumber())
          && Objects.equals(this.getName(), other.getName())
          && Objects.equals(this.getAddress(), other.getAddress())
          && Objects.equals(this.getZipCode(), other.getZipCode())
          && Objects.equals(this.getCity(), other.getCity())
          && Objects.equals(this.getCountry(), other.getCountry())
          && Objects.equals(this.getEmail(), other.getEmail())
          && Objects.equals(this.getExternalId(), other.getExternalId())
          && Objects.equals(this.getPhone(), other.getPhone());
    }
  }

  public String getAddress() {
    return getString("address");
  }

  public String getCity() {
    return getString("city");
  }

  /** See {@link CustomerField#COUNTRY} for more details. */
  public String getCountry() {
    return getString("country");
  }

  public String getEmail() {
    return getString("email");
  }

  /** See {@link #setExternalId(String)} for more details. */
  public String getExternalId() {
    return getString("externalId");
  }

  /** See {@link #setGid(String)} for more details. */
  public String getId() {
    return getString("gid");
  }

  public String getName() {
    return getString("name");
  }

  public String getPhone() {
    return getString("phone");
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
    return getString("vatNumber");
  }

  public String getZipCode() {
    return getString("zipCode");
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.getVatNumber(),
        this.getName(),
        this.getAddress(),
        this.getZipCode(),
        this.getCity(),
        this.getCountry(),
        this.getId(),
        this.getEmail(),
        this.getExternalId(),
        this.getPhone());
  }

  public void setAddress(String address) {
    fields.put("address", address);
  }

  public void setCity(String city) {
    fields.put("city", city);
  }

  /** See {@link CustomerField#COUNTRY} for more details. */
  public void setCountry(String country) {
    fields.put("country", country);
  }

  public void setEmail(String email) {
    fields.put("email", email);
  }

  /**
   * Set the external id of the model. The external id can be for example the id of the
   * corresponding model in the local database.
   *
   * @param externalId the external id
   */
  public void setExternalId(String externalId) {
    fields.put("externalId", externalId);
  }

  /**
   * The id of the model in the external database (as returned by any request).
   *
   * @param id the id.
   */
  public void setGid(String id) {
    fields.put("gid", id);
  }

  public void setName(String name) {
    fields.put("name", name);
  }

  public void setPhone(String phone) {
    fields.put("phone", phone);
  }

  public void setVatNumber(String vatNumber) {
    fields.put("vatNumber", vatNumber);
  }

  public void setZipCode(String zipCode) {
    fields.put("zipCode", zipCode);
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
