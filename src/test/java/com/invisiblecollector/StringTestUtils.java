package com.invisiblecollector;

import com.invisiblecollector.model.json.JsonSingleton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class StringTestUtils {

  private StringTestUtils() {}

  
  public static String randomHexString() {
    String string = UUID.randomUUID().toString();
    string.replaceAll("-", "");
    return string;
  }
  
  public static String dateToString(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat(JsonSingleton.DATE_FORMAT);
    return formatter.format(date);
  }
}
