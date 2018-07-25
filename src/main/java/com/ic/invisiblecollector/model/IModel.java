package com.ic.invisiblecollector.model;

import java.util.EnumMap;

/**
 * Implementing classes represent the data in the remote database.
 * 
 * @author ros
 */
public interface IModel {
  /**
   * Converts the object into the appropriate enum map. 
   * Null values are removed.
   * 
   * @return the map of the attributes
   */
  EnumMap<? extends Enum<?>, Object> toEnumMap();
}
