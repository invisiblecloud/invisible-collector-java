package com.invisiblecollector.model;

import java.util.*;

public abstract class Model {

  protected Map<String, Object> fields = new HashMap<>();

  protected void pmdWorkaround() {}

  protected Double getDouble(String key) {
    return (Double) fields.get(key);
  }

  protected Date getDate(String key) {
    return (Date) fields.get(key);
  }

  protected Map<String, String> getStringMap(String key) {
    return (Map<String, String>) fields.get(key);
  }

  @Override
  public int hashCode() {
    return fields.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Model)) {
      return false;
    } else if (this == obj) {
      return true;
    } else {
      Model other = (Model) obj;
      return fields.equals(other.fields);
    }
  }

  protected String getString(String key) {
    return (String) fields.get(key);
  }

  protected Boolean getBoolean(String key) {
    return (Boolean) fields.get(key);
  }

  public Map<String, Object> getFields() {
    return new HashMap<>(fields);
  }

  public void assertConstainsKeys(String... keys) {
    Arrays.stream(keys)
        .filter(key -> !fields.containsKey(key))
        .forEach(
            key -> {
              String msg = String.format("Field %s is missing", key);
              throw new IllegalArgumentException(msg);
            });
  }

  public Map<String, Object> getOnlyFields(String... keys) {
    Map<String, Object> copy = getFields();
    List<String> keyList = Arrays.asList(keys);

    copy.entrySet().removeIf(entry -> !keyList.contains(entry.getKey()));

    return copy;
  }
}
