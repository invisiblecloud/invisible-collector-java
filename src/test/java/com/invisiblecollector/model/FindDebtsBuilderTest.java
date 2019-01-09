package com.invisiblecollector.model;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class FindDebtsBuilderTest {

  @Test
  public void withFromDate_order() {
    final String errorMsg = "to_date must follow from_date";

    Date firstDate = new Date(1, 1, 1);
    Date lastDate = new Date(2, 1, 1);
    Date zeroDay = new Date(0, 1, 1);

    FindDebtsBuilder builder = new FindDebtsBuilder().withFromDate(firstDate).withToDate(lastDate);

    IllegalArgumentException e =
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.withToDate(zeroDay));
    MatcherAssert.assertThat(e.getMessage(), CoreMatchers.containsString(errorMsg));

    FindDebtsBuilder builder2 = new FindDebtsBuilder().withToDate(firstDate);
    IllegalArgumentException e2 =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> builder2.withFromDate(lastDate));
    MatcherAssert.assertThat(e2.getMessage(), CoreMatchers.containsString(errorMsg));
  }

  @Test
  public void withFromDueDate_order() {
    final String errorMsg = "to_duedate must follow from_duedate";

    Date firstDate = new Date(1, 1, 1);
    Date lastDate = new Date(2, 1, 1);
    Date zeroDay = new Date(0, 1, 1);

    FindDebtsBuilder builder =
        new FindDebtsBuilder().withFromDueDate(firstDate).withToDueDate(lastDate);

    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> builder.withToDueDate(zeroDay));
    MatcherAssert.assertThat(e.getMessage(), CoreMatchers.containsString(errorMsg));

    FindDebtsBuilder builder2 = new FindDebtsBuilder().withToDueDate(firstDate);
    IllegalArgumentException e2 =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> builder2.withFromDueDate(lastDate));
    MatcherAssert.assertThat(e2.getMessage(), CoreMatchers.containsString(errorMsg));
  }
}
