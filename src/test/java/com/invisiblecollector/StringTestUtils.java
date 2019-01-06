package com.invisiblecollector;

import com.invisiblecollector.model.serialization.JsonSingleton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class StringTestUtils {

  private StringTestUtils() {}

  
  public static String randomHexString() {
    return UUID.randomUUID().toString();
  }

}
