package com.ic.invoicecapture.model;

public interface ICustomerUpdate extends IRoutable {
  String getAddress();

  String getCity();

  String getCountry();

  String getEmail();

  String getName();

  String getPhone();

  String getVatNumber();

  String getZipCode();
}
