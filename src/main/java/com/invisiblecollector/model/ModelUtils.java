package com.invisiblecollector.model;

import java.util.Map;

final class ModelUtils {
  static <T> void tryAddObject(Map<T, Object> map, T key, Object value) {
    if (value != null && key != null) {
      map.put(key, value);
    }
  }
}
