package com.invisiblecollector.model.builder;

import java.util.*;

import com.invisiblecollector.StringTestUtils;
import com.invisiblecollector.model.Debt;
import com.invisiblecollector.model.Item;

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
    public Map<String, Object> buildObject() {
        Map<String, Object> jsonObject = buildSendableObject();

        jsonObject.put("id", id);

        return jsonObject;
    }

    private class ExtraDebt extends Debt {
        private ExtraDebt(Map<String, Object> map) {
            fields = map;
        }
    }

    public Debt buildModel() {
        return new ExtraDebt(buildObject());
    }

    @Override
    public Map<String, Object> buildSendableObject() {
        Map<String, Object> jsonObject = new HashMap<>();

        jsonObject.put("number", number);
        jsonObject.put("customerId", customerId);
        jsonObject.put("type", type);
        jsonObject.put("status", status);
        jsonObject.put("date", StringTestUtils.dateToString(date));
        jsonObject.put("dueDate", StringTestUtils.dateToString(dueDate));
        jsonObject.put("netTotal", netTotal);
        jsonObject.put("tax", tax);
        jsonObject.put("grossTotal", grossTotal);
        jsonObject.put("currency", currency);
        jsonObject.put("attributes", StringTestUtils.toJsonElement(getAttributes()));
        jsonObject.put("items", StringTestUtils.toJsonElement(getItems()));

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
