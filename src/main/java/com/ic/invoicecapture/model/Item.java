package com.ic.invoicecapture.model;

import java.util.EnumMap;
import com.ic.invoicecapture.model.DebtField.ItemField;

public class Item implements IModel {

  private String description;
  private String name;
  private Double price;
  private Double quantity;
  private Double vat;

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  public Double getPrice() {
    return price;
  }

  public Double getQuantity() {
    return quantity;
  }

  public Double getVat() {
    return vat;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  public void setVat(Double vat) {
    this.vat = vat;
  }

  @Override
  public EnumMap<ItemField, Object> toEnumMap() {
    EnumMap<ItemField, Object> map = new EnumMap<>(ItemField.class);

    ModelUtils.tryAddObject(map, ItemField.NAME, getName());
    ModelUtils.tryAddObject(map, ItemField.DESCRIPTION, getDescription());
    ModelUtils.tryAddObject(map, ItemField.QUANTITY, getQuantity());
    ModelUtils.tryAddObject(map, ItemField.VAT, getVat());
    ModelUtils.tryAddObject(map, ItemField.PRICE, getPrice());

    return map;
  }
}