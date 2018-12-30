package com.invisiblecollector.model.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;


public class JsonSingletonTest {

  @Test 
  public void getInstance_correctness() {
    Gson gson1 = JsonSingleton.getInstance();
    Gson gson2 = JsonSingleton.getInstance();
    
    Assertions.assertEquals(gson1, gson2);
  }
}
