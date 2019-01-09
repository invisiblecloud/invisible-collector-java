package com.invisiblecollector;

import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

public final class StringTestUtils {

  private StringTestUtils() {}

  public static String randomHexString() {
    return UUID.randomUUID().toString();
  }

  public static String mapToUrlQuery(Map<String, Object> fields) {
    return fields
        .entrySet()
        .stream()
        .map(p -> URLEncoder.encode(p.getKey()) + "=" + URLEncoder.encode(p.getValue().toString()))
        .reduce((p1, p2) -> p1 + "&" + p2)
        .orElse("");
  }
}
