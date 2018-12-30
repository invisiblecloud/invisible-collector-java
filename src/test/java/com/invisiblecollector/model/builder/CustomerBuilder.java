package com.invisiblecollector.model.builder;

import com.invisiblecollector.model.Customer;
import com.invisiblecollector.model.Debt;

import java.util.HashMap;
import java.util.Map;

public class CustomerBuilder extends BuilderBase {

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

  public CustomerBuilder() {}

  public CustomerBuilder(String address, String city, String country, String email,
      String externalId, String gid, String name, String phone, String vatNumber, String zipCode) {
    this.address = address;
    this.city = city;
    this.country = country;
    this.email = email;
    this.externalId = externalId;
    this.setGid(gid);
    this.name = name;
    this.phone = phone;
    this.vatNumber = vatNumber;
    this.zipCode = zipCode;

  }

  private class ExtraCustomer extends Customer {
    private ExtraCustomer(Map<String, Object> map) {
      fields = map;
    }
  }

  public Customer buildModel() {

    return new ExtraCustomer(buildObject());
  }

  public static CustomerBuilder buildTestCustomerBuilder() {
    return new CustomerBuilder("testAdress", "testCity", "PT", "testEmail@gmail.com", "12345",
        "234", "testName", "9999", "509784852", "23123");
  }
  
  @Override
  public Map<String, Object> buildSendableObject() {
    Map<String, Object> jsonObject = new HashMap<>();

    jsonObject.put("name", name);
    jsonObject.put("externalId", externalId);
    jsonObject.put("vatNumber", vatNumber);
    jsonObject.put("address", address);
    jsonObject.put("zipCode", zipCode);
    jsonObject.put("city", city);
    jsonObject.put("country", country);
    jsonObject.put("email", email);
    jsonObject.put("phone", phone);

    return jsonObject;
  }

  @Override
  public Map<String, Object> buildObject() {
    Map<String, Object>  jsonObject = buildSendableObject();

    jsonObject.put("gid", gid);

    return jsonObject;
  }

  public CustomerBuilder setAddress(String address) {
    this.address = address;
    return this;
  }

  public CustomerBuilder setCity(String city) {
    this.city = city;
    return this;
  }

  public CustomerBuilder setCountry(String country) {
    this.country = country;
    return this;
  }

  public CustomerBuilder setEmail(String email) {
    this.email = email;
    return this;
  }

  public CustomerBuilder setExternalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

  public CustomerBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public CustomerBuilder setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public CustomerBuilder setVatNumber(String vatNumber) {
    this.vatNumber = vatNumber;
    return this;
  }

  public CustomerBuilder setZipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  public CustomerBuilder setGid(String gid) {
    this.gid = gid;
    return this;
  }


  public String getExternalId() {
    return gid;
  }

}
