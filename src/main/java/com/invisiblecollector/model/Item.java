package com.invisiblecollector.model;

import com.invisiblecollector.exceptions.IcRuntimeException;

import java.util.HashMap;

/** A model for debt items. */
public class Item extends Model implements Cloneable {

  @Override
  public int hashCode() {
    pmdWorkaround();
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Item)) {
      return false;
    } else if (this == obj) {
      return true;
    } else {
      Item other = (Item) obj;
      return super.equals(other);
    }
  }

  public String getDescription() {
    return getString("description");
  }

  public String getName() {
    return getString("name");
  }

  public Double getPrice() {
    return getDouble("price");
  }

  public Double getQuantity() {
    return getDouble("quantity");
  }

  public Double getVat() {
    return getDouble("vat");
  }

  public void setDescription(String description) {
    fields.put("description", description);
  }

  public void setName(String name) {
    fields.put("name", name);
  }

  /**
   * Set the item's price
   *
   * @param price the price. Default value is 0.0
   */
  public void setPrice(Double price) {
    fields.put("price", price);
  }

  /**
   * Set the quantity.
   *
   * @param quantity The amount of items of this type included in the transaction. Default value is 0.0
   */
  public void setQuantity(Double quantity) {
    fields.put("quantity", quantity);
  }

  /**
   * Set the item's VAT.
   *
   * @param vat the VAT. Default value is 0.0
   */
  public void setVat(Double vat) {
    fields.put("vat", vat);
  }

  /**
   * Create a deep clone
   *
   * @return deep clone of the model
   */
  @Override
  public Item clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      throw new IcRuntimeException(e);
    }
    Item copy = new Item();
    copy.fields = new HashMap<>(fields);
    return copy;
  }
}
