package com.ic.invoicecapture.json;

import com.google.gson.Gson;

public class GsonSingleton {

  private static Gson instance = null;

  public static Gson getInstance() {
    if (instance == null) {
      instance = new Gson();
    }
    
    return instance;
  }
}
