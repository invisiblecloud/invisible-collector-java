package com.invisiblecollector.model.serialization;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class UriEncodingFacade {

  private static String uriEncodeString(String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }

  private static String uriEncodeValue(Object value) {
    String str;
    if (value instanceof Date) {
      Date date = (Date) value;
      SimpleDateFormat formatter = new SimpleDateFormat(StringUtils.DATE_FORMAT);
      str = formatter.format(date);
    } else {
      str = value.toString();
    }

    return uriEncodeString(str);
  }

  public static String encodeUri(Map<String, Object> fields) {
    return fields
        .entrySet()
        .stream()
        .map(e -> uriEncodeString(e.getKey()) + "=" + uriEncodeValue(e.getValue()))
        .collect(Collectors.joining("&"));
  }
}
