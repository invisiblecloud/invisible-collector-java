package com.ic.invoicecapture.json;

import com.google.gson.Gson;

public class GsonSingleton {

  private static Gson instance = null;

  private GsonSingleton() {
    instance = new Gson();
  }

  public static Gson getInstance() {
    return instance;
  }
}
