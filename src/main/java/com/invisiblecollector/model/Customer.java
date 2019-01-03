package com.invisiblecollector.model;

/**
 * A model for the customer.
 *
 * @author ros
 */
public class Customer extends Model implements IRoutable {

  @Override
  public int hashCode() {
    pmdWorkaround();
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Customer)) {
      return false;
    } else if (this == obj) {
      return true;
    } else {
      Customer other = (Customer) obj;
      return super.equals(other);
    }
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



  /**
   * Set the email.
   *
   * @param email the email. It's validated for correctness
   */
  public void setEmail(String email) {
    if (email != null && !email.contains("@")) {
      throw new IllegalArgumentException("email must have email format");
    }

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
}
