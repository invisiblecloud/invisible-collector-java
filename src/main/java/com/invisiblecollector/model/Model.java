package com.invisiblecollector.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The base model.
 *
 * <p>Any initial model instance starts without any fields set (converting to serialization renders
 * an empty object). Any setter can either set a value or a null (to allow sending serialization
 * object with null values).
 */
public abstract class Model {

  protected Map<String, Object> fields = new HashMap<>();

  /** @return the hash code */
  @Override
  public int hashCode() {
    return fields.hashCode();
  }

  /**
   * Compare for equality.
   *
   * @param obj the other object
   * @return true on equal
   */
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

  public Map<String, Object> getFields() {
    return new HashMap<>(fields);
  }

  public void assertContainsKeys(String... keys) {
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

  protected String getString(String key) {
    return (String) fields.get(key);
  }

  protected Boolean getBoolean(String key) {
    return (Boolean) fields.get(key);
  }

  protected void assertCountryIso3166(String country) {
    if (country != null && country.length() != 2 && country.length() != 3) {
      throw new IllegalArgumentException("country must be in ISO 3166-1 format");
    }
  }

  protected void assertDateOrder(Date date, Date dueDate, String msg) {
    if (date != null && dueDate != null && date.compareTo(dueDate) > 0) {
      throw new IllegalArgumentException(msg);
    }
  }

  @Override
  public String toString() {
    return "{"
        + fields
            .entrySet()
            .stream()
            .map(e -> e.getKey() + ": " + e.getValue())
            .collect(Collectors.joining(", "))
        + "}";
  }
}
