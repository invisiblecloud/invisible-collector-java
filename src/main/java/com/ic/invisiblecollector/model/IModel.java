package com.ic.invisiblecollector.model;

import java.util.EnumMap;

public interface IModel {
  EnumMap<? extends Enum<?>, Object> toEnumMap();
}
