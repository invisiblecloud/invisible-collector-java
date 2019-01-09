package com.invisiblecollector.model.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JsonSingleton {

  private static ObjectMapper instance = null;

  public static ObjectMapper getInstance() {
    if (instance == null) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      DateFormat df = new SimpleDateFormat(StringUtils.DATE_FORMAT);
      mapper.setDateFormat(df);
      instance = mapper;
    }

    return instance;
  }

}
