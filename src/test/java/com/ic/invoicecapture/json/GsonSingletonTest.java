package com.ic.invoicecapture.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import com.ic.invoicecapture.model.json.GsonSingleton;


public class GsonSingletonTest {

  @Test 
  public void getInstance_correctness() {
    Gson gson1 = GsonSingleton.getInstance();
    Gson gson2 = GsonSingleton.getInstance();
    
    Assertions.assertEquals(gson1, gson2);
  }
}
