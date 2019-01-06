package com.invisiblecollector.model;

import com.invisiblecollector.model.serialization.StringUtils;

import java.util.Date;

public class FindDebtsBuilder extends Model {

  public FindDebtsBuilder withFromDate(Date fromDate) {
    assertDateOrder(fromDate, getToDate(), "to_date must follow from_date");
    fields.put("from_date", StringUtils.dateToString(fromDate));
    return this;
  }

  public FindDebtsBuilder withToDate(Date toDate) {
    assertDateOrder(getFromDate(), toDate, "to_date must follow from_date");
    fields.put("to_date", StringUtils.dateToString(toDate));
    return this;
  }

  public FindDebtsBuilder withFromDueDate(Date fromDueDate) {
    assertDateOrder(fromDueDate, getToDueDate(), "to_duedate must follow from_duedate");
    fields.put("from_duedate", StringUtils.dateToString(fromDueDate));
    return this;
  }

  public FindDebtsBuilder withToDueDate(Date toDueDate) {
    assertDateOrder(getFromDueDate(), toDueDate, "to_duedate must follow from_duedate");
    fields.put("to_duedate", StringUtils.dateToString(toDueDate));
    return this;
  }

  public FindDebtsBuilder withNumber(String number) {
    fields.put("number", number);
    return this;
  }

  public Date getToDueDate() {
    return StringUtils.parseDateString(getString("to_duedate"));
  }

  public Date getFromDueDate() {
    return StringUtils.parseDateString(getString("from_duedate"));
  }

  public Date getFromDate() {
    return StringUtils.parseDateString(getString("from_date"));
  }

  public Date getToDate() {
    return StringUtils.parseDateString(getString("to_date"));
  }

  public String getNumber() {
    return getString("number");
  }

}
