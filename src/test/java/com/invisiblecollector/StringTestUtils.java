package com.invisiblecollector;

import com.invisiblecollector.model.json.JsonSingleton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class StringTestUtils {

  private StringTestUtils() {}

  
  public static String randomHexString() {
    return UUID.randomUUID().toString();
  }
  
  public static String dateToString(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat(JsonSingleton.DATE_FORMAT);
    return formatter.format(date);
  }
}
