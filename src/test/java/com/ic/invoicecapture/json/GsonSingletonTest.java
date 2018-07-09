package com.ic.invoicecapture.json;

import org.junit.Test;
import com.google.gson.Gson;
import org.junit.Assert;

public class GsonSingletonTest {

  @Test 
  public void getInstance_correctness() {
    Gson gson1 = GsonSingleton.getInstance();
    Gson gson2 = GsonSingleton.getInstance();
    
    Assert.assertEquals(gson1, gson2);
  }
}
