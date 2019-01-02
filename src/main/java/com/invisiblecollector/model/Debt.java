package com.invisiblecollector.model;

import java.util.*;
import java.util.stream.Collectors;

public class Debt extends Model implements IModel, IRoutable {
  private interface ItemList extends List<Item> {};

  public void addAttribute(String key, String value) {
    Map<String, String> attributes = getStringMap("attributes");

    if (attributes == null) {
      attributes = new HashMap<>();
      fields.put("attributes", (attributes));
    }

    attributes.put(key, value);
  }

  public void addItem(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("item cannot be null");
    }

    List<Item> items = getItemsInternals();

    if (items == null) {
      items = new ArrayList<>();
      fields.put("items", items);
    }

    items.add(item);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Debt)) {
      return false;
    } else if (this == obj) {
      return true;
    } else {
      Debt other = (Debt) obj;
      return Objects.equals(this.getAttributes(), other.getAttributes())
          && Objects.equals(this.getCurrency(), other.getCurrency())
          && Objects.equals(this.getCustomerId(), other.getCustomerId())
          && Objects.equals(this.getDate(), other.getDate())
          && Objects.equals(this.getDueDate(), other.getDueDate())
          && Objects.equals(this.getGrossTotal(), other.getGrossTotal())
          && Objects.equals(this.getId(), other.getId())
          && Objects.equals(this.getItems(), other.getItems())
          && Objects.equals(this.getNetTotal(), other.getNetTotal())
          && Objects.equals(this.getNumber(), other.getNumber())
          && Objects.equals(this.getStatus(), other.getStatus())
          && Objects.equals(this.getTax(), other.getTax());
    }
  }

  public Map<String, String> getAttributes() {
    Map<String, String> attributes = getStringMap("attributes");
    if (attributes == null) {
      return null;
    }
    return new HashMap<>(attributes);
  }

  /** See {@link DebtField#CURRENCY} for more details. */
  public String getCurrency() {
    return getString("currency");
  }

  public String getCustomerId() {
    return getString("customerId");
  }

  /** See {@link DebtField#DATE} for more details. */
  public Date getDate() {
    return new Date(getDate("date").getTime());
  }

  /** See {@link DebtField#DUE_DATE} for more details. */
  public Date getDueDate() {
    return new Date(getDate("dueDate").getTime());
  }

  public Double getGrossTotal() {
    return getDouble("grossTotal");
  }

  public String getId() {
    return getString("id");
  }

  public List<Item> getItems() {
    List<Item> items = getItemsInternals();
    if (items == null) {
      return null;
    }
    return new ArrayList<>(items);
  }

  private List<Item> getItemsInternals() {
    return (ItemList) fields.get("items");
  }

  public Double getNetTotal() {
    return getDouble("netTotal");
  }

  public String getNumber() {
    return getString("number");
  }

  @Override
  public String getRoutableId() {
    return getId();
  }

  /** See {@link DebtField#STATUS} for more details. */
  public String getStatus() {
    return getString("status");
  }

  public Double getTax() {
    return getDouble("tax");
  }

  /** See {@link DebtField#TYPE} for more details. */
  public String getType() {
    return getString("type");
  }

  @Override
  public int hashCode() {
    return Objects.hash(
//        get
        getCurrency(),
        getCustomerId(),
        getDate(),
        getDueDate(),
        getGrossTotal(),
        getId(),
        getNetTotal(),
        getNumber(),
        getStatus(),
        getTax());
  }

  public void setAttributes(Map<String, String> attributes) {
    fields.put("attributes", new HashMap<>(attributes));
  }

  /** See {@link DebtField#CURRENCY} for more details. */
  public void setCurrency(String currency) {
    fields.put("currency", currency);
  }

  /**
   * See {@link DebtField#CUSTOMER_ID} for more details.
   *
   * @param customerInfo the object that has the id or external id of the customer. Can be a {@link
   *     Customer} object.
   * @see #setCustomerId(String)
   */
  public void setCustomerId(IRoutable customerInfo) {
    setCustomerId(customerInfo.getRoutableId());
  }

  /**
   * See {@link DebtField#CUSTOMER_ID} for more details.
   *
   * @see #setCustomerId(IRoutable)
   */
  public void setCustomerId(String customerId) {
    fields.put("customerId", customerId);
  }

  /** See {@link DebtField#DATE} for more details. */
  public void setDate(Date date) {
    fields.put("date", new Date(date.getTime()));
  }

  /** See {@link DebtField#DUE_DATE} for more details. */
  public void setDueDate(Date dueDate) {
    fields.put("dueDate", new Date(dueDate.getTime()));
  }

  public void setGrossTotal(Double grossTotal) {
    fields.put("grossTotal", grossTotal);
  }

  public void setId(String id) {
    fields.put("id", id);
  }

  public void setItems(List<Item> items) {
    List<Item> copy = new ArrayList<>(items);
    fields.put("items", copy);
  }

  public void setNetTotal(Double netTotal) {
    fields.put("netTotal", netTotal);
  }

  public void setNumber(String number) {
    fields.put("number", number);
  }

  /** See {@link DebtField#STATUS} for possible values. */
  public void setStatus(String status) {
    fields.put("status", status);
  }

  public void setTax(Double tax) {
    fields.put("tax", tax);
  }

  /** See {@link DebtField#TYPE} for possible values. */
  public void setType(String type) {
    fields.put("type", type);
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
