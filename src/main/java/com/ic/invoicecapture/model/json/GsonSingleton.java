package com.ic.invoicecapture.model.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSingleton {

  private static Gson instance = null;

  public static Gson getInstance() {
    if (instance == null) {
      instance = new GsonBuilder().serializeNulls().create();
    }
    
    return instance;
  }
}
