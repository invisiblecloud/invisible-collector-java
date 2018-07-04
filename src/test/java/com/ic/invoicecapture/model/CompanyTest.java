package com.ic.invoicecapture.model;

import org.junit.Assert;
import org.junit.Test;
import com.google.gson.JsonObject;
import com.ic.invoicecapture.json.JsonConversion;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.builder.CompanyBuilder;

public class CompanyTest {
  
  private static CompanyBuilder buildTestCompanyBuilder() {
    return new CompanyBuilder(
        "testAdress", 
        "testCity", 
        "testCountry", 
        "testGid", 
        "testName", 
        false, 
        "testVatNumber", 
        "testZipCode"
        );
  }
  
  @Test
  public void equals_empty() {
    
    Company company1 = new Company();
    Company company2 = new Company();
    
    Assert.assertEquals(company1, company2);
    
    company1.setName("some other name");
    Assert.assertNotEquals(company1, company2);
  }
  
  @Test 
  public void toJson_jsonCorrectness() {
    CompanyBuilder companyBuilder = CompanyTest.buildTestCompanyBuilder();
    
    Company company = companyBuilder.buildCompany();
    String generatedJsonString = company.asJsonString();
    JsonObject generatedJson = JsonConversion.jsonStringAsJsonObject(generatedJsonString);
    JsonObject correctJson = companyBuilder.buildJsonObject();
    
    Assert.assertEquals(generatedJson, correctJson);
    
    company.setCity("newCity");
    generatedJsonString = company.asJsonString();
    generatedJson = JsonConversion.jsonStringAsJsonObject(generatedJsonString);
    Assert.assertNotEquals(generatedJson, correctJson);
  }
}
