package com.invisiblecollector.model.builder;

import com.invisiblecollector.model.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemBuilder extends BuilderBase {
  
  private String description;
  private String name;
  private Double price;
  private Double quantity;
  private Double vat;

  public static ItemBuilder buildAnotherTestItemBuilder() {
    return buildTestItemBuilder().setName("a differenet name");
  }
  
  public static ItemBuilder buildTestItemBuilder() {
    return new ItemBuilder().setDescription("a description").setName("A Name")
        .setPrice(123.0).setQuantity(23d).setVat(24d);
  }

  @Override
  public Map<String, Object> buildObject() {
    return buildSendableObject();
  }

  private class ExtraItem extends Item {
    private ExtraItem(Map<String, Object> map) {
       fields = map;
    }
  }

  public Item buildModel() {
    return new ExtraItem(buildObject());
  }

  @Override
  protected Map<String, Object> buildSendableObject() {
    Map<String, Object> jsonObject = new HashMap<>();

    jsonObject.put("description", description);
    jsonObject.put("name", name);
    jsonObject.put("price", price);
    jsonObject.put("quantity", quantity);
    jsonObject.put("vat", vat);



    return jsonObject;
  }

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

  public ItemBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public ItemBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public ItemBuilder setPrice(Double price) {
    this.price = price;
    return this;
  }

  public ItemBuilder setQuantity(Double quantity) {
    this.quantity = quantity;
    return this;
  }

  public ItemBuilder setVat(Double vat) {
    this.vat = vat;
    return this;
  }

}