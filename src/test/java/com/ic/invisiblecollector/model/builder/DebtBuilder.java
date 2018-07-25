package com.ic.invisiblecollector.model.builder;

import com.google.gson.JsonObject;
import com.ic.invisiblecollector.StringTestUtils;
import com.ic.invisiblecollector.model.Debt;
import com.ic.invisiblecollector.model.Item;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DebtBuilder extends BuilderBase {

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

  private static Date buildDate(int yearOffset) {
    Calendar calendarNow = Calendar.getInstance();
    calendarNow.add(Calendar.YEAR, yearOffset);

    return calendarNow.getTime();
  }

  public static DebtBuilder buildMinimalTestBuilder() {
    return new DebtBuilder().setNumber("1").setCustomerId("1").setType("FS")
        .setDate(buildDate(0)).setDueDate(buildDate(2));
  }

  public DebtBuilder addItem(Item item) {
    if (items == null) {
      items = new ArrayList<>();
    }

    items.add(item);
    return this;
  }

  public static DebtBuilder buildTestDebtBuilder() {
    Map<String, String> attributes = new TreeMap<>();
    attributes.put("test-key", "test-value");
    attributes.put("key2", "value2");

    return new DebtBuilder().setNumber("2").setCustomerId("1").setType("FS").setDate(buildDate(0))
        .setDueDate(buildDate(2)).setAttributes(attributes)
        .addItem(ItemBuilder.buildTestItemBuilder().buildModel())
        .addItem(ItemBuilder.buildAnotherTestItemBuilder().buildModel()).setId("1");
  }

  @Override
  public JsonObject buildJsonObject() {
    JsonObject jsonObject = buildSendableJsonObject();

    jsonObject.addProperty("id", id);

    return jsonObject;
  }

  @Override
  public Debt buildModel() {
    return buildModel(Debt.class);
  }

  @Override
  public JsonObject buildSendableJsonObject() {
    JsonObject jsonObject = new JsonObject();

    jsonObject.addProperty("number", number);
    jsonObject.addProperty("customerId", customerId);
    jsonObject.addProperty("type", type);
    jsonObject.addProperty("status", status);
    jsonObject.addProperty("date", StringTestUtils.dateToString(date));
    jsonObject.addProperty("dueDate", StringTestUtils.dateToString(dueDate));
    jsonObject.addProperty("netTotal", netTotal);
    jsonObject.addProperty("tax", tax);
    jsonObject.addProperty("grossTotal", grossTotal);
    jsonObject.addProperty("currency", currency);
    jsonObject.add("attributes", StringTestUtils.toJsonElement(getAttributes()));
    jsonObject.add("items", StringTestUtils.toJsonElement(getItems()));

    return jsonObject;
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

  public DebtBuilder setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
    return this;
  }

  public DebtBuilder setCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public DebtBuilder setCustomerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  public DebtBuilder setDate(Date date) {
    this.date = date;
    return this;
  }

  public DebtBuilder setDueDate(Date dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  public DebtBuilder setGrossTotal(Double grossTotal) {
    this.grossTotal = grossTotal;
    return this;
  }

  public DebtBuilder setId(String id) {
    this.id = id;
    return this;
  }

  public DebtBuilder setItems(List<Item> items) {
    this.items = items;
    return this;
  }

  public DebtBuilder setNetTotal(Double netTotal) {
    this.netTotal = netTotal;
    return this;
  }

  public DebtBuilder setNumber(String number) {
    this.number = number;
    return this;
  }

  public DebtBuilder setStatus(String status) {
    this.status = status;
    return this;
  }

  public DebtBuilder setTax(Double tax) {
    this.tax = tax;
    return this;
  }

  public DebtBuilder setType(String type) {
    this.type = type;
    return this;
  }

}
