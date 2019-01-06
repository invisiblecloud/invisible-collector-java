package com.invisiblecollector;

import java.util.UUID;

public final class StringTestUtils {

  private StringTestUtils() {}

  
  public static String randomHexString() {
    return UUID.randomUUID().toString();
  }

}
