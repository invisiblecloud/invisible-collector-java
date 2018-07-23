package com.ic.invoicecapture.model.builder;

import com.google.gson.JsonObject;
import com.ic.invoicecapture.StringTestUtils;
import com.ic.invoicecapture.model.Debt;
import com.ic.invoicecapture.model.json.JsonTestUtils;
import java.util.Calendar;
import java.util.Date;

public class DebtBuilder extends BuilderBase {
  private String currency;
  private String customerId;
  private Date date;
  private Date dueDate;
  private Double grossTotal;
  private String id;
  private Double netTotal;
  private String number;
  private String status;
  private Double tax;
  private String type;

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
    // TODO: add attributes

    return jsonObject;
  }

//  public static DebtBuilder buildTestDebtBuilder() {
//    return new DebtBuilder("123", "1", "PT", "testEmail@gmail.com", "12345",
//        "234", "testName", "9999", "509784852", "23123");
//  }
  
  private static Date buildDate(int yearOffset) {
    Calendar calendarNow = Calendar.getInstance();
    calendarNow.add(Calendar.YEAR, yearOffset);
    
    return calendarNow.getTime();
  }
  
  public static DebtBuilder buildMinimalTestBuilder() {
    return new DebtBuilder().setNumber("1").setCustomerId("1").setType("FS")
        .setDate(buildDate(0)).setDueDate(buildDate(2));
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
