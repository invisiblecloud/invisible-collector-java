package com.ic.invoicecapture.model.json;

import com.google.gson.JsonObject;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.IModel;
import com.ic.invoicecapture.model.builder.CompanyBuilder;
import com.ic.invoicecapture.model.json.JsonModelFacade;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonModelFacadeTest {
  
  
  @Test
  public void parseStringStream_companyInstance() throws IcException {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    Company correctCompany = companyBuilder.buildCompany();
    
    String jsonString = companyBuilder.buildJsonObject().toString();
    
    InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
    
    JsonModelFacade jsonFacade = new JsonModelFacade();
    Company returnedCompany = jsonFacade.parseStringStream(inputStream, Company.class);
    
    Assertions.assertEquals(correctCompany, returnedCompany);
  }
  
  @Test
  public void toJson_IModel_correctness() {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    
    Company correctCompany = companyBuilder.buildCompany();
    String returnedJson = new JsonModelFacade()
        .toJson((IModel) correctCompany);
    
    JsonObject returnedJsonObj = JsonConverter.jsonStringAsJsonObject(returnedJson);
    JsonObject correctCompanyJsonObj = companyBuilder.buildJsonObject();
    Assertions.assertEquals(returnedJsonObj, correctCompanyJsonObj);
  }
  
}
