package com.invisiblecollector.model;

public class Item extends Model {

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

  public void setPrice(Double price) {
    fields.put("price", price);
  }

  public void setQuantity(Double quantity) {
    fields.put("quantity", quantity);
  }

  public void setVat(Double vat) {
    fields.put("vat", vat);
  }
}
