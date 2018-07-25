package com.ic.invisiblecollector.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
  private String type;

  public void addItem(Item item) {
    if (items == null) {
      items = new ArrayList<>();
    }
    
    if (item == null) {
      throw new IllegalArgumentException("item cannot be null");
    }

    items.add(item);
  }

  public void addAttribute(String key, String value) {
    if (attributes == null) {
      attributes = new HashMap<>();
    }
    
    attributes.put(key, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Debt)) {
      return false;
    } else if (this == obj) {
      return true;
    } else {
      Debt other = (Debt) obj;
      return Objects.equals(this.attributes, other.attributes)
          && Objects.equals(this.currency, other.currency)
          && Objects.equals(this.customerId, other.customerId)
          && Objects.equals(this.date, other.date) && Objects.equals(this.dueDate, other.dueDate)
          && Objects.equals(this.grossTotal, other.grossTotal) && Objects.equals(this.id, other.id)
          && Objects.equals(this.items, other.items)
          && Objects.equals(this.netTotal, other.netTotal)
          && Objects.equals(this.number, other.number) && Objects.equals(this.status, other.status)
          && Objects.equals(this.tax, other.tax);
    }
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  /**
   * See {@link DebtField#CURRENCY} for more details. 
   */
  public String getCurrency() {
    return currency;
  }

  public String getCustomerId() {
    return customerId;
  }

  /**
   * See {@link DebtField#DATE} for more details. 
   */
  public Date getDate() {
    return new Date(date.getTime());
  }

  /**
   * See {@link DebtField#DUE_DATE} for more details. 
   */
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

  /**
   * See {@link DebtField#STATUS} for more details. 
   */
  public String getStatus() {
    return status;
  }

  public Double getTax() {
    return tax;
  }

  /**
   * See {@link DebtField#TYPE} for more details. 
   */
  public String getType() {
    return type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributes, currency, customerId, date, dueDate, grossTotal, id, items,
        netTotal, number, status, tax);
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  /**
   * See {@link DebtField#CURRENCY} for more details. 
   */
  public void setCurrency(String currency) {
    this.currency = currency;
  }

  /**
   * See {@link DebtField#CUSTOMER_ID} for more details.
   * 
   * @see #setCustomerId(IRoutable)
   */
  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
  
  /**
   * See {@link DebtField#CUSTOMER_ID} for more details.
   * 
   * @param customerInfo the object that has the id or external id of the customer.
   *        Can be a {@link Customer} object.
   * @see #setCustomerId(String)
   */
  public void setCustomerId(IRoutable customerInfo) {
    this.customerId = customerInfo.getRoutableId();
  }

  /**
   * See {@link DebtField#DATE} for more details. 
   */
  public void setDate(Date date) {
    this.date = new Date(date.getTime());
  }

  /**
   * See {@link DebtField#DUE_DATE} for more details. 
   */
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

  /**
   * See {@link DebtField#STATUS} for possible values. 
   */
  public void setStatus(String status) {
    this.status = status;
  }

  public void setTax(Double tax) {
    this.tax = tax;
  }

  /**
   * See {@link DebtField#TYPE} for possible values. 
   */
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

  @Override
  public String getRoutableId() {
    // TODO Auto-generated method stub
    return null;
  }
}
