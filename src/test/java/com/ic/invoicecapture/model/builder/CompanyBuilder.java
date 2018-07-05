package com.ic.invoicecapture.model.builder;

import com.google.gson.JsonObject;
import com.ic.invoicecapture.model.Company;

public class CompanyBuilder {

  private String address = null;
  private String city = null;
  private String country = null;
  private String gid = null;
  private String name = null;
  private Boolean notificationsEnabled = null;
  private String vatNumber = null;
  private String zipCode = null;

  public Company buildCompany() {
    Company company = new Company();

    company.setName(name);
    company.setGid(gid);
    company.setVatNumber(vatNumber);
    company.setAddress(address);
    company.setZipCode(zipCode);
    company.setCity(city);
    company.setCountry(country);
    company.setNotificationsEnabled(notificationsEnabled);

    return company;
  }

  public CompanyBuilder(String address, String city, String country, String gid, String name,
      Boolean notificationsEnabled, String vatNumber, String zipCode) {
    this.address = address;
    this.city = city;
    this.country = country;
    this.gid = gid;
    this.name = name;
    this.notificationsEnabled = notificationsEnabled;
    this.vatNumber = vatNumber;
    this.zipCode = zipCode;

  }

  public JsonObject buildJsonObject() {
    JsonObject jsonObject = new JsonObject();

    jsonObject.addProperty("address", address);
    jsonObject.addProperty("city", city);
    jsonObject.addProperty("country", country);
    jsonObject.addProperty("gid", gid);
    jsonObject.addProperty("name", name);
    jsonObject.addProperty("notificationsEnabled", notificationsEnabled);
    jsonObject.addProperty("vatNumber", vatNumber);
    jsonObject.addProperty("zipCode", zipCode);

    return jsonObject;
  }

  public CompanyBuilder setAddress(String address) {
    this.address = address;
    return this;
  }

  public CompanyBuilder setCity(String city) {
    this.city = city;
    return this;
  }

  public CompanyBuilder setCountry(String country) {
    this.country = country;
    return this;
  }

  public CompanyBuilder setGid(String gid) {
    this.gid = gid;
    return this;
  }

  public CompanyBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public CompanyBuilder setNotificationsEnabled(Boolean notificationsEnabled) {
    this.notificationsEnabled = notificationsEnabled;
    return this;
  }

  public CompanyBuilder setVatNumber(String vatNumber) {
    this.vatNumber = vatNumber;
    return this;
  }

  public CompanyBuilder setZipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }



}