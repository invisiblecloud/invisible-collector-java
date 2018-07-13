package com.ic.invoicecapture.json;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.builder.CompanyBuilder;
import com.ic.invoicecapture.model.json.JsonModelFacade;

public class JsonFacadeTest {
  @Test
  public void stringStreamToJsonObject_companyInstance() throws IcException {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    Company correctCompany = companyBuilder.buildCompany();
    
    String jsonString = companyBuilder.buildJsonObject().toString();
    
    InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
    JsonModelFacade jsonFacade = new JsonModelFacade();
    
    Company returnedCompany = jsonFacade.parseStringStream(inputStream, Company.class);
    
    Assertions.assertEquals(correctCompany, returnedCompany);
  }
}
