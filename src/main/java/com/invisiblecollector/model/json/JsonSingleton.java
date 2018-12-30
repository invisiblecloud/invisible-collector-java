package com.invisiblecollector.model.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSingleton {

  private static Gson instance = null;
  /**
   * Follows ISO 8601 cropped to day without timezones.
   */
  public static final String DATE_FORMAT = "yyyy-MM-dd"; // https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html?is-external=true
  
  public static Gson getInstance() {
    if (instance == null) {
      instance = new GsonBuilder().serializeNulls().setDateFormat(DATE_FORMAT).create();
    }
    
    return instance;
  }

  private static ObjectMapper instance2 = null;

  public static ObjectMapper getInstance2() {
    if (instance2 == null) {
      instance2 = new ObjectMapper();
      instance2.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    return instance2;
  }

}
