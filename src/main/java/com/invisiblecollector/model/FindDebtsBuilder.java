package com.invisiblecollector.model;

import java.util.Date;

public class FindDebtsBuilder extends Model {

  public FindDebtsBuilder withFromDate(Date fromDate) {
    assertDateOrder(fromDate, getDate("to_date"), "to_date must follow from_date");
    fields.put("from_date", new Date(fromDate.getTime()));
    return this;
  }

  public FindDebtsBuilder withToDate(Date toDate) {
    assertDateOrder(getDate("from_date"), toDate, "to_date must follow from_date");
    fields.put("to_date", new Date(toDate.getTime()));
    return this;
  }

  public FindDebtsBuilder withFromDueDate(Date fromDueDate) {
    assertDateOrder(fromDueDate, getDate("to_duedate"), "to_duedate must follow from_duedate");
    fields.put("from_duedate", new Date(fromDueDate.getTime()));
    return this;
  }

  public FindDebtsBuilder withToDueDate(Date toDueDate) {
    assertDateOrder(getDate("from_duedate"), toDueDate, "to_duedate must follow from_duedate");
    fields.put("to_duedate", new Date(toDueDate.getTime()));
    return this;
  }

  public FindDebtsBuilder withNumber(String number) {
    fields.put("number", number);
    return this;
  }
}
