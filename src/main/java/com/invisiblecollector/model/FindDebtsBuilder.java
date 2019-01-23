package com.invisiblecollector.model;

import java.util.Date;

/**
 * Model for searching debts
 *
 * <p>Implements a fluent builder API.
 */
public class FindDebtsBuilder extends Model {

  private static final String DATE_ERROR_MSG = "to_date must follow from_date";

  /**
   * Set search start date
   *
   * @param fromDate the date. time is ignored.
   * @return this
   * @throws IllegalArgumentException if there is a to_date set with an earlier date
   */
  public FindDebtsBuilder withFromDate(Date fromDate) {

    assertDateOrder(fromDate, getToDate(), DATE_ERROR_MSG);
    setDate("from_date", fromDate);
    return this;
  }

  /**
   * Set search end date
   *
   * @param toDate the date. time is ignored.
   * @return this
   * @throws IllegalArgumentException if there is a from_date with a later date
   */
  public FindDebtsBuilder withToDate(Date toDate) {
    assertDateOrder(getFromDate(), toDate, DATE_ERROR_MSG);
    setDate("to_date", toDate);
    return this;
  }

  private static final String DUE_DATE_ERROR_MSG = "to_duedate must follow from_duedate";

  /**
   * Set search start due date
   *
   * @param fromDueDate the date. time is ignored.
   * @return this
   * @throws IllegalArgumentException if there is a to_duedate with an earlier date
   */
  public FindDebtsBuilder withFromDueDate(Date fromDueDate) {

    assertDateOrder(fromDueDate, getToDueDate(), DUE_DATE_ERROR_MSG);
    setDate("from_duedate", fromDueDate);
    return this;
  }

  /**
   * Set search end due date
   *
   * @param toDueDate the date. time is ignored.
   * @return this
   * @throws IllegalArgumentException if there is a from_duedate with a later date
   */
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

  @Override
  public int hashCode() {
    pmdWorkaround();
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof FindDebtsBuilder)) {
      return false;
    } else if (this == obj) {
      return true;
    } else {
      return super.equals(obj);
    }
  }
}
