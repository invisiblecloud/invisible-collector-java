package com.invisiblecollector.model;

import com.invisiblecollector.model.serialization.StringUtils;

import java.util.Date;

public class FindDebtsBuilder extends Model {

  public FindDebtsBuilder withFromDate(Date fromDate) {
    assertDateOrder(fromDate, getToDate(), "to_date must follow from_date");
    setDate("from_date", fromDate);
    return this;
  }

  public FindDebtsBuilder withToDate(Date toDate) {
    assertDateOrder(getFromDate(), toDate, "to_date must follow from_date");
    setDate("to_date", toDate);
    return this;
  }

  public FindDebtsBuilder withFromDueDate(Date fromDueDate) {
    assertDateOrder(fromDueDate, getToDueDate(), "to_duedate must follow from_duedate");
    setDate("from_duedate", fromDueDate);
    return this;
  }

  public FindDebtsBuilder withToDueDate(Date toDueDate) {
    assertDateOrder(getFromDueDate(), toDueDate, "to_duedate must follow from_duedate");
    setDate("to_duedate", toDueDate);
    return this;
  }

  public FindDebtsBuilder withNumber(String number) {
    fields.put("number", number);
    return this;
  }

  public Date getToDueDate() {
    return getDate("to_duedate");
  }

  public Date getFromDueDate() {
    return getDate("from_duedate");
  }

  public Date getFromDate() {
    return getDate("from_date");
  }

  public Date getToDate() {
    return getDate("to_date");
  }

  public String getNumber() {
    return getString("number");
  }

}
