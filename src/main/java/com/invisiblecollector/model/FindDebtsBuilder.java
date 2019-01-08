package com.invisiblecollector.model;

import java.util.Date;

public class FindDebtsBuilder extends Model {

  private static final String DATE_ERROR_MSG = "to_date must follow from_date";
  public FindDebtsBuilder withFromDate(Date fromDate) {

    assertDateOrder(fromDate, getToDate(), DATE_ERROR_MSG);
    setDate("from_date", fromDate);
    return this;
  }

  public FindDebtsBuilder withToDate(Date toDate) {
    assertDateOrder(getFromDate(), toDate, DATE_ERROR_MSG);
    setDate("to_date", toDate);
    return this;
  }

  private static final String DUE_DATE_ERROR_MSG = "to_duedate must follow from_duedate";
  public FindDebtsBuilder withFromDueDate(Date fromDueDate) {

    assertDateOrder(fromDueDate, getToDueDate(), DUE_DATE_ERROR_MSG);
    setDate("from_duedate", fromDueDate);
    return this;
  }

  public FindDebtsBuilder withToDueDate(Date toDueDate) {
    assertDateOrder(getFromDueDate(), toDueDate, DUE_DATE_ERROR_MSG);
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
