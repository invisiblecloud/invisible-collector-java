package com.invisiblecollector.model;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

final class FieldEnumUtils {
  @SafeVarargs // mandatoryEnums should be safe, need to check
  static <T extends ICheckableField> void assertCorrectlyInitializedEnumMap(Map<T, Object> map,
      String modelName, T... mandatoryEnums) throws IllegalArgumentException {
    for (T mandatoryEnum : mandatoryEnums) {
      if (!map.containsKey(mandatoryEnum)) {
        String msg =
            String.format("field '%s' of Model '%s' MUST be present", mandatoryEnum, modelName);
        throw new IllegalArgumentException(msg);
      }
    }

    for (Map.Entry<T, Object> entry : map.entrySet()) {
      Object value = entry.getValue();
      T key = entry.getKey();
      try {
        key.assertValueIsValid(value);
      } catch (IllegalArgumentException e) {
        String msg = String.format("For '%s' model's field '%s' value is invalid: %s", modelName,
            key, e.getMessage());
        throw new IllegalArgumentException(msg, e);
      }
    }
  }

  public static void assertStringObject(Object value) throws IllegalArgumentException {
    if (value != null && !(value instanceof String)) {
      throw new IllegalArgumentException("value must be String");
    }
  }

  public static void assertNumberObject(Object value) throws IllegalArgumentException {
    if (value != null && !(value instanceof Float) && !(value instanceof Double)
        && !(value instanceof Integer)) {
      throw new IllegalArgumentException("value must be a Float, Double or Integer type");
    }
  }


  public static void assertDateObject(Object value) throws IllegalArgumentException {
    if (value != null && !(value instanceof Date)) {
      throw new IllegalArgumentException("value must be of type Date");
    }
  }

  public static void assertStringMapObject(Object value) throws IllegalArgumentException {
    if (value == null) {
      return;
    } else if (value instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<Object, Object> map = (Map<Object, Object>) value;
      for (Entry<Object, Object> entry  : map.entrySet()) {
        Object key = entry.getKey();
        Object pairValue = entry.getValue();
        final boolean isKeyString = key instanceof String;
        final boolean isValueString = pairValue instanceof String;
        if (!isKeyString || !isValueString) {
          String msg = !isKeyString ? String.format("key (%s)", key)
              : String.format("value (%s)", pairValue) + " must be of type String";
          throw new IllegalArgumentException(msg);
        }
      }
    } else {
      throw new IllegalArgumentException("Value must be a Map type");
    }
  }
}
