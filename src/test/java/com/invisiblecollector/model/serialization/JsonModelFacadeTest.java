package com.invisiblecollector.model.serialization;

import com.invisiblecollector.exceptions.IcException;
import com.invisiblecollector.model.Company;
import com.invisiblecollector.model.builder.CompanyBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

public class JsonModelFacadeTest {

  private static String TEST_MAP_JSON = "{ \"a\":12, \"b\":\"string\", \"c\":null }";
  private static String CORRECT_MAP_JSON = "{ \"a\":\"12\", \"b\":\"string\", \"c\":null }";
  private static final String TEST_JSON_DATE = "\"2013-03-19\"";
  private static Map<String, String> CORRECT_MAP = new TreeMap<>();
  
  static {
    CORRECT_MAP.put("a", "12");
    CORRECT_MAP.put("b", "string");
    CORRECT_MAP.put("c", null);
  }

  private InputStream stringToInputStream(String string) {
    return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void parseStringStream_companyInstance() throws IcException {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    Company correctCompany = companyBuilder.buildModel();

    String jsonString = companyBuilder.buildJson();

    InputStream inputStream = stringToInputStream(jsonString);

    JsonModelFacade jsonFacade = new JsonModelFacade();
    Company returnedCompany = jsonFacade.parseStringStream(inputStream, Company.class);

    Assertions.assertEquals(correctCompany, returnedCompany);
  }
  
  @Test
  public void parseStringStream_debtInstance() throws IcException {
    Date correctDate = new GregorianCalendar(2013, 2, 19).getTime();
    InputStream inputStream = stringToInputStream(TEST_JSON_DATE);
    Date returnedDate = new JsonModelFacade().parseStringStream(inputStream, Date.class);
    Assertions.assertEquals(correctDate, returnedDate);
  }

  @Test
  public void parseStringStreamAsStringMap_correctness() throws IcException {


    InputStream inputStream = stringToInputStream(TEST_MAP_JSON);
    Map<String, String> map = new JsonModelFacade().parseStringStreamAsStringMap(inputStream);
    Assertions.assertEquals(CORRECT_MAP, map);
  }
}
