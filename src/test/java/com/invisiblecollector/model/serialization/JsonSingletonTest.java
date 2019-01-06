package com.invisiblecollector.model.serialization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class JsonSingletonTest {

  @Test 
  public void getInstance_correctness() {
    Object json1 = JsonSingleton.getInstance();
    Object json2 = JsonSingleton.getInstance();
    
    Assertions.assertEquals(json1, json2);
  }
}
