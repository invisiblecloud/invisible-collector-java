package com.ic.invoicecapture.json;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.builder.CompanyBuilder;

public class JsonFacadeTest {
  @Test
  public void stringStreamToJsonObject_companyInstance() throws UnsupportedEncodingException {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    Company correctCompany = companyBuilder.buildCompany();
    
    String jsonString = companyBuilder.buildJsonObject().toString();
    
    InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
    JsonFacade jsonFacade = new JsonFacade();
    
    Company returnedCompany = jsonFacade.stringStreamToJsonObject(inputStream, Company.class);
    
    Assertions.assertEquals(correctCompany, returnedCompany);
  }
}
