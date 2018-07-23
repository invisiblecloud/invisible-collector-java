package com.ic.invoicecapture.model.builder;

import com.google.gson.JsonObject;
import com.ic.invoicecapture.model.Debt.Item;

public class ItemBuilder extends BuilderBase {
  
  private String description;
  private String name;
  private Double price;
  private Double quantity;
  private Double vat;

  public static ItemBuilder buildTestItemBuilder() {
    return new ItemBuilder().setDescription("a description").setName("A Name")
        .setPrice(123.0).setQuantity(23d).setVat(24d);
  }

  @Override
  public JsonObject buildJsonObject() {
    return buildSendableJsonObject();
  }

  @Override
  public Item buildModel() {
    return buildModel(Item.class);
  }

  @Override
  public JsonObject buildSendableJsonObject() {
    JsonObject jsonObject = new JsonObject();

    jsonObject.addProperty("description", description);
    jsonObject.addProperty("name", name);
    jsonObject.addProperty("price", price);
    jsonObject.addProperty("quantity", quantity);
    jsonObject.addProperty("vat", vat);

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