package com.ic.invoicecapture.model.builder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ic.invoicecapture.model.Customer;
import com.ic.invoicecapture.model.json.GsonSingleton;

public class CustomerBuilder {

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


  public Customer buildCustomer() {

    String json = this.buildJsonObject().toString();

    Gson gson = GsonSingleton.getInstance();
    return gson.fromJson(json, Customer.class);
  }

  public static CustomerBuilder buildTestCustomerBuilder() {
    return new CustomerBuilder("testAdress", "testCity", "PT", "testEmail@gmail.com", "12345",
        "234", "testName", "9999", "509784852", "23123");
  }

  public JsonObject buildJsonObject() {
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
    jsonObject.addProperty("gid", gid);

    return jsonObject;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
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

  public void setGid(String gid) {
    this.gid = gid;
  }

}
