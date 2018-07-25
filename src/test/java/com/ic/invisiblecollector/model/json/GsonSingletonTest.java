package com.ic.invisiblecollector.model.json;

import com.google.gson.Gson;
import com.ic.invisiblecollector.model.json.GsonSingleton;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class GsonSingletonTest {

  @Test 
  public void getInstance_correctness() {
    Gson gson1 = GsonSingleton.getInstance();
    Gson gson2 = GsonSingleton.getInstance();
    
    Assertions.assertEquals(gson1, gson2);
  }
}
