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
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonModelFacadeTest {

  private InputStream stringToInputStream(String string) {
    return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void parseStringStream_companyInstance() throws IcException {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    Company correctCompany = companyBuilder.buildCompany();

    String jsonString = companyBuilder.buildJsonObject().toString();

    InputStream inputStream = stringToInputStream(jsonString);

    JsonModelFacade jsonFacade = new JsonModelFacade();
    Company returnedCompany = jsonFacade.parseStringStream(inputStream, Company.class);

    Assertions.assertEquals(correctCompany, returnedCompany);
  }

  @Test
  public void toJson_IModel_correctness() {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();

    Company correctCompany = companyBuilder.buildCompany();
    String returnedJson = new JsonModelFacade().toJson((IModel) correctCompany);

    JsonObject returnedJsonObj = JsonTestUtils.jsonStringAsJsonObject(returnedJson);
    JsonObject correctCompanyJsonObj = companyBuilder.buildJsonObject();
    Assertions.assertEquals(returnedJsonObj, correctCompanyJsonObj);
  }

  private static String TEST_JSON = "{ \"a\":12, \"b\":\"string\", \"c\":null }";
  private static String CORRECT_JSON = "{ \"a\":\"12\", \"b\":\"string\", \"c\":null }";
  private static Map<String, String> CORRECT_MAP = new TreeMap<>();
  
  static {
    CORRECT_MAP.put("a", "12");
    CORRECT_MAP.put("b", "string");
    CORRECT_MAP.put("c", null);
  }

  @Test
  public void parseStringStreamAsStringMap_correctness() throws IcException {


    InputStream inputStream = stringToInputStream(TEST_JSON);
    Map<String, String> map = new JsonModelFacade().parseStringStreamAsStringMap(inputStream);
    Assertions.assertEquals(CORRECT_MAP, map);
  }

  @Test
  public void toJson_stringMap_correctness() {
    String returnedJson = new JsonModelFacade().toJson(CORRECT_MAP);
    JsonTestUtils.assertJsonEquals(CORRECT_JSON, returnedJson);
  }
}
