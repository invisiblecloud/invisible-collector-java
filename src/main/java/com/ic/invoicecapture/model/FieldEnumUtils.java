package com.ic.invoicecapture.model;

import java.util.Map;

final class FieldEnumUtils {
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
      if (!key.isValidValue(value)) {
        String msg = String.format("value (%s) or type (%s) for key (%s) is not valid",
            value, value.getClass().getName(), key);
        throw new IllegalArgumentException(msg);
      }
    }
  }
}
