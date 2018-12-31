package com.invisiblecollector.model.json;

import com.invisiblecollector.StringTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;


public class JsonSingletonTest {

  @Test 
  public void getInstance_correctness() {
    Gson gson1 = StringTestUtils.getInstance();
    Gson gson2 = StringTestUtils.getInstance();
    
    Assertions.assertEquals(gson1, gson2);
  }
}