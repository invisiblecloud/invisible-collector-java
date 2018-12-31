package com.invisiblecollector.model.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JsonSingleton {

  /**
   * Follows ISO 8601 cropped to day without timezones.
   */
  public static final String DATE_FORMAT = "yyyy-MM-dd"; // https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html?is-external=true

  private static ObjectMapper instance2 = null;

  public static ObjectMapper getInstance2() {
    if (instance2 == null) {
      instance2 = new ObjectMapper();
      instance2.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      DateFormat df = new SimpleDateFormat(DATE_FORMAT);
      instance2.setDateFormat(df);
    }

    return instance2;
  }

}
