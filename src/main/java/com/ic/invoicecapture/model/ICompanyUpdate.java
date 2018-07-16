package com.ic.invoicecapture.model;

/**
 * Used to update company info.
 * To update only some attributes return null in the attribute getters to ignore.
 * 
 * @author ros
 */
public interface ICompanyUpdate {
  String getAddress();

  String getCity();

  String getName();

  String getVatNumber();

  String getZipCode();
}
