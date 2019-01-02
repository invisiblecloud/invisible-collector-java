package com.invisiblecollector.model;

import com.invisiblecollector.model.builder.CompanyBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompanyTest {

  @Test
  public void equals_empty() {

    Company company1 = new Company();
    Company company2 = new Company();

    Assertions.assertEquals(company1, company2);

    company1.setName("12321");
    Assertions.assertNotEquals(company1, company2);
  }
  
  @Test
  public void equals_null() {

    Company company = new Company();

    Assertions.assertNotEquals(company, null);
  }
  
  @Test
  public void equals_seeded() {
    CompanyBuilder builder = CompanyBuilder.buildTestCompanyBuilder();
    Company company1 = builder.buildModel();
    Company company2 = builder.buildModel();
    
    Assertions.assertEquals(company1, company2);
    
    company2.setCity("some city");
    Assertions.assertNotEquals(company1, company2);
  }
  
  @Test
  public void equals_identity() {
    CompanyBuilder builder = CompanyBuilder.buildTestCompanyBuilder();
    Company company = builder.buildModel();
    Assertions.assertEquals(company, company);
  }

  @Test
  public void hashCode_correctness() {
    Company company1 = new Company();
    Company company2 = new Company();

    Assertions.assertEquals(company1.hashCode(), company2.hashCode());

    company1.setName("some other name");
    Assertions.assertNotEquals(company1.hashCode(), company2.hashCode());
  }
  
}
