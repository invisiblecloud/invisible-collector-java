package com.ic.invisiblecollector.model;

import java.util.Map;

public final class ModelUtils {
  static <T> void tryAddObject(Map<T, Object> map, T key, Object value) {
    if (value != null && key != null) {
      map.put(key, value);
    }
  }
  
  public static String getAndAssertCorrectId(IInternallyRoutable container) throws IllegalArgumentException {
    String gid = container.getId();
    String externalId = container.getExternalId();
    if (gid != null && !gid.isEmpty()) {
      return gid;
    } else if (externalId != null && !externalId.isEmpty()) {
      return externalId;
    } else {
      throw new IllegalArgumentException("no valid id contained in object");
    }
  }
}
