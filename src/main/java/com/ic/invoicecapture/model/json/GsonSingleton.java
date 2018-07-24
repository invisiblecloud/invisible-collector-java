package com.ic.invoicecapture.model.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSingleton {

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
}
