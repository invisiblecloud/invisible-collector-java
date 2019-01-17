package com.invisiblecollector.model.serialization;

import com.invisiblecollector.exceptions.IcRuntimeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
  /** Follows ISO 8601 cropped to day without timezones. */
  public static final String DATE_FORMAT =
      "yyyy-MM-dd"; // https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html?is-external=true

  public static String dateToString(Date date) {
    if (date == null) {
      return null;
    }

    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    return formatter.format(date);
  }

  public static Date parseDateString(String dateString) {
      if (dateString == null) {
          return null;
      }

      SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
      try {
          return formatter.parse(dateString);
      } catch (ParseException e) {
          throw new IcRuntimeException(e);
      }
  }
}
