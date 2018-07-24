package com.ic.invoicecapture.model;

import com.ic.invoicecapture.model.builder.DebtBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DebtTest {
  @Test
  public void equals_empty() {

    Debt debt1 = new Debt();
    Debt debt2 = new Debt();

    Assertions.assertEquals(debt1, debt2);

    debt1.setCustomerId("909090909");
    Assertions.assertNotEquals(debt1, debt2);
  }
  
  @Test
  public void equals_null() {

    Debt debt = new Debt();

    Assertions.assertNotEquals(debt, null);
  }
  
  @Test
  public void equals_seeded() {
    DebtBuilder builder = DebtBuilder.buildTestDebtBuilder();
    Debt debt1 = builder.buildModel();
    Debt debt2 = builder.buildModel();
    
    Assertions.assertEquals(debt1, debt2);
    
    debt2.setCustomerId("909090909");
    Assertions.assertNotEquals(debt1, debt2);
  }
  
  @Test
  public void equals_identity() {
    DebtBuilder builder = DebtBuilder.buildTestDebtBuilder();
    Debt debt = builder.buildModel();
    Assertions.assertEquals(debt, debt);
  }

  @Test
  public void hashCode_correctness() {
    Debt debt1 = new Debt();
    Debt debt2 = new Debt();

    Assertions.assertEquals(debt1.hashCode(), debt2.hashCode());

    debt1.setCustomerId("909090909");
    Assertions.assertNotEquals(debt1.hashCode(), debt2.hashCode());
  }
}
