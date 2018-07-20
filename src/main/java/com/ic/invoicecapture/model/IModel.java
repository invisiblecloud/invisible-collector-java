package com.ic.invoicecapture.model;

import java.util.EnumMap;

public interface IModel {
  EnumMap<? extends Enum<?>, Object> toEnumMap();
}
