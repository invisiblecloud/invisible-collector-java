package com.invisiblecollector.model.builder;

import com.google.gson.JsonObject;
import com.invisiblecollector.model.Customer;

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

  @Override
  public Customer buildModel() {

    return buildModel(Customer.class);
  }

  public static CustomerBuilder buildTestCustomerBuilder() {
    return new CustomerBuilder("testAdress", "testCity", "PT", "testEmail@gmail.com", "12345",
        "234", "testName", "9999", "509784852", "23123");
  }
  
  @Override
  public JsonObject buildSendableJsonObject() {
    JsonObject jsonObject = new JsonObject();

    jsonObject.addProperty("name", name);
    jsonObject.addProperty("externalId", externalId);
    jsonObject.addProperty("vatNumber", vatNumber);
    jsonObject.addProperty("address", address);
    jsonObject.addProperty("zipCode", zipCode);
    jsonObject.addProperty("city", city);
    jsonObject.addProperty("country", country);
    jsonObject.addProperty("email", email);
    jsonObject.addProperty("phone", phone);

    return jsonObject;
  }

  @Override
  public JsonObject buildJsonObject() {
    JsonObject jsonObject = buildSendableJsonObject();

    jsonObject.addProperty("gid", gid);

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
