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

  private static ObjectMapper instance = null;
  public static ObjectMapper getInstance() {
    if (instance == null) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      DateFormat df = new SimpleDateFormat(DATE_FORMAT);
      mapper.setDateFormat(df);
      instance = mapper;
    }

    return instance;
  }

}
