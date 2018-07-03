package com.ic.invoicecapture.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import com.ic.invoicecapture.model.Company;

public class CompanyTest {

  @Test
  public void testEquals() {
    Company c1 = new Company();
    Company c2 = new Company();
    assertEquals(c1, c2);
    c1.setName("some other name");
    assertNotEquals(c1, c2);
  }
}
