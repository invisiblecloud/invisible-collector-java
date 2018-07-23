package com.ic.invoicecapture.model;

import com.ic.invoicecapture.model.DebtField.ItemField;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Debt implements IModel, IRoutable {
  private Map<String, String> attributes;
  private String currency;
  private String customerId;
  private Date date;
  private Date dueDate;
  private Double grossTotal;
  private String id;
  private List<Item> items;
  private Double netTotal;
  private String number;
  private String status;
  private Double tax;

  /**
   * Type can be one of "FS", "SD", "RC", "RG" or "DG".
   */
  private String type;

  public void addItem(Item item) {
    if (items == null) {
      items = new ArrayList<>();
    }

    items.add(item);
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public String getCurrency() {
    return currency;
  }

  public String getCustomerId() {
    return customerId;
  }

  public Date getDate() {
    return new Date(date.getTime());
  }

  public Date getDueDate() {
    return new Date(dueDate.getTime());
  }

  public Double getGrossTotal() {
    return grossTotal;
  }

  public String getId() {
    return id;
  }

  public List<Item> getItems() {
    return items;
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

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public void setDate(Date date) {
    this.date = new Date(date.getTime());
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = new Date(dueDate.getTime());
  }

  public void setGrossTotal(Double grossTotal) {
    this.grossTotal = grossTotal;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setItems(List<Item> items) {
    this.items = items;
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
    ModelUtils.tryAddObject(map, DebtField.ATTRIBUTES, getAttributes());
    if (getItems() != null) {
      List<Map<ItemField, Object>> processedItems =
          getItems().stream().map(item -> item.toEnumMap()).collect(Collectors.toList());
      map.put(DebtField.ITEMS, processedItems);
    }

    return map;
  }
}
