package com.invisiblecollector.model;

import java.util.*;
import java.util.stream.Collectors;

public class Debt extends Model implements IRoutable {
  public void addAttribute(String key, String value) {
    Map<String, String> attributes = getStringMap("attributes");

    if (attributes == null) {
      attributes = new HashMap<>();
      fields.put("attributes", attributes);
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

    items.add(item.clone());
  }

  @Override
  public int hashCode() {
    pmdWorkaround();
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Debt)) {
      return false;
    } else if (this == obj) {
      return true;
    } else {
      Debt other = (Debt) obj;
      return super.equals(other);
    }
  }

  public Map<String, String> getAttributes() {
    Map<String, String> attributes = getStringMap("attributes");
    if (attributes == null) {
      return new HashMap<>();
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

  private static List<Item> copyItemsList(List<Item> items) {
    return items.stream().map(Item::clone).collect(Collectors.toList());
  }

  public List<Item> getItems() {
    List<Item> items = getItemsInternals();
    if (items == null) {
      return new ArrayList<>();
    }

    return copyItemsList(items);
  }

  private List<Item> getItemsInternals() {
    return (List<Item>) fields.get("items");
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

  public void setAttributes(Map<String, String> attributes) {
    if (attributes != null) {
      attributes = new HashMap<>(attributes);
    }
    fields.put("attributes", attributes);
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

  /**
   * Set the items
   *
   * @param items the items to set. items are deep cloned before setting.
   */
  public void setItems(List<Item> items) {
    if (items != null) {
      items = copyItemsList(items);
    }
    fields.put("items", items);
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

  /** @param tax The total tax amount. */
  public void setTax(Double tax) {
    fields.put("tax", tax);
  }

  /**
   * Set the type
   *
   * @param type The debt type. Expected value is a {@link String} Value must be one of: <br>
   *     "FT" - Normal invoice; <br>
   *     "FS" - Simplified invoice; <br>
   *     "SD" - Standard debt; <br>
   *     Check <a href="https://www.invisiblecollector.com/docs/api/debts/post/">the API docs </a>
   *     for up to date acceptable values
   */
  public void setType(String type) {
    fields.put("type", type);
  }
}
