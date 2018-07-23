package com.ic.invoicecapture.model;

import java.util.Date;
import java.util.EnumMap;

public class Debt implements IModel, IRoutable {
  // TODO: add fields
  // items
  // attributes
  private String currency;
  private String customerId;
  private Date date;
  private Date dueDate;
  private Double grossTotal;
  private String id;
  private Double netTotal;
  private String number;
  private String status;
  private Double tax;
  /** Can be
   * "FS", // simplified invoice
            "SD", // simple debt
            "RC", "RG", "DG"
   */
  private String type;

  public String getCurrency() {
    return currency;
  }

  public String getCustomerId() {
    return customerId;
  }

  public Date getDate() {
    return date;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public Double getGrossTotal() {
    return grossTotal;
  }

  public String getId() {
    return id;
  }

  public Double getNetTotal() {
    return netTotal;
  }

  public String getNumber() {
    return number;
  }

  public String getStatus() {
    return status;
  }

  public Double getTax() {
    return tax;
  }

  public String getType() {
    return type;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public void setGrossTotal(Double grossTotal) {
    this.grossTotal = grossTotal;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setNetTotal(Double netTotal) {
    this.netTotal = netTotal;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setTax(Double tax) {
    this.tax = tax;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public EnumMap<DebtField, Object> toEnumMap() {
    EnumMap<DebtField, Object> map = new EnumMap<>(DebtField.class);

    ModelUtils.tryAddObject(map, DebtField.NUMBER, getNumber());
    ModelUtils.tryAddObject(map, DebtField.CUSTOMER_ID, getCustomerId());
    ModelUtils.tryAddObject(map, DebtField.TYPE, getType());
    ModelUtils.tryAddObject(map, DebtField.STATUS, getStatus());
    ModelUtils.tryAddObject(map, DebtField.DATE, getDate());
    ModelUtils.tryAddObject(map, DebtField.DUE_DATE, getDueDate());
    ModelUtils.tryAddObject(map, DebtField.NET_TOTAL, getNetTotal());
    ModelUtils.tryAddObject(map, DebtField.TAX, getTax());
    ModelUtils.tryAddObject(map, DebtField.GROSS_TOTAL, getGrossTotal());
    ModelUtils.tryAddObject(map, DebtField.CURRENCY, getCurrency());
    // TODO add items and attributes
    
    return map;
  }
}
