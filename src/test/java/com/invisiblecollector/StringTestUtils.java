package com.invisiblecollector;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.invisiblecollector.model.json.GsonSingleton;

public final class StringTestUtils {
  private StringTestUtils() {}

  // provided by Pavel Repin @ https://stackoverflow.com/a/5445161
  public static String inputStreamToString(InputStream inputStream) {
    Scanner scanner = new Scanner(inputStream, "UTF-8");
    scanner.useDelimiter("\\A");
    String value = scanner.hasNext() ? scanner.next() : "";
    scanner.close();
    return value;
  }
  
  public static String randomHexString() {
    String string = UUID.randomUUID().toString();
    string.replaceAll("-", "");
    return string;
  }
  
  public static String dateToString(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat(GsonSingleton.DATE_FORMAT);
    return formatter.format(date);
  }

  public static JsonElement toJsonElement(Object obj) {
    return GsonSingleton.getInstance().toJsonTree(obj);
  }

}
