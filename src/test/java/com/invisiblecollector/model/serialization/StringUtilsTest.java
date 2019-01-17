package com.invisiblecollector.model.serialization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class StringUtilsTest {

  public static final Date buildDate(int year, int month, int day) {
    return Date.from(LocalDateTime.of(year, month, day, 0, 0).toInstant(ZoneOffset.UTC));
  }

  @Test
  public void dateToString_correctness() {
    Date date = buildDate(2123, 1, 12);
    String text = StringUtils.dateToString(date);
    Assertions.assertEquals("2123-01-12", text);
  }

  @Test
  public void parseDateString_correctness() {
    Date expected = buildDate(2012, 1, 12);
    Date actual = StringUtils.parseDateString("2012-01-12");

    Assertions.assertEquals(expected, actual);
  }
}
