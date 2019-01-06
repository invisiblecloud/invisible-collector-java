package com.ic.invisiblecollector;

import com.invisiblecollector.IcApiFacade;
import com.invisiblecollector.model.Company;
import com.invisiblecollector.model.FindDebtsBuilder;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Date;

@SuppressWarnings("deprecation")
public class IcFacadeManualTest {

  private static final String TEST_API_TOKEN =
      "470398d51aaf443b1154259b0032fcee9189f925051c93b2c88517d22abcc8f1";
  private static final URI BASE_URL =
      URI.create(
          //          "https://api.nxt.invisiblecollector.com"
          "http://localhost:8080");

  private static IcApiFacade buildIcFacade() {
    return new IcApiFacade(TEST_API_TOKEN, BASE_URL);
  }

  @Test
  public void getCompany() throws Exception {
    IcApiFacade facade = buildIcFacade();
    Company compnay = facade.requestCompanyInfo();
    System.out.println(compnay);
  }

  @Test
  public void debtFind() throws Exception {

    IcApiFacade facade = buildIcFacade();
    FindDebtsBuilder builder = new FindDebtsBuilder();
    builder.withFromDate(new Date(110, 0, 1));
    builder.withToDate(new Date(114, 9, 1));
    System.out.println(facade.findDebts(builder));

  }

  //  public static void putCompany() throws Exception {
  //    CompanyApiFacade facade = buildIcFacade().getCompanyFacade();
  //    Company companyUpdate = CompanyBuilder.buildTestCompanyBuilder().buildModel();
  //    companyUpdate.setCity("A whole new city");
  //    companyUpdate.setName("testing a new name");
  //    companyUpdate.setVatNumber("509451837");
  //    Company compnay = facade.updateCompanyInfo(companyUpdate.toEnumMap());
  //    printModel(compnay);
  //  }
  //
  //  public static void setCompanyNotifications(boolean enableNotifications) throws Exception {
  //    CompanyApiFacade facade = buildIcFacade().getCompanyFacade();
  //    Company compnay = facade.setCompanyNotifications(enableNotifications);
  //    printModel(compnay);
  //  }
  //
  //  public static void postGetCustomer() throws Exception {
  //    CustomerApiFacade facade = buildIcFacade().getCustomerFacade();
  //    Customer costumerInfo = CustomerBuilder.buildTestCustomerBuilder().buildModel();
  //    costumerInfo.setVatNumber("503287822");
  //    costumerInfo.setExternalId("1234431");
  //    Customer customer = facade.registerNewCustomer(costumerInfo);
  //    printModel(customer);
  //  }
  //
  //  public static void getCustomer() throws Exception {
  //    CustomerApiFacade facade = buildIcFacade().getCustomerFacade();
  //    Customer customer2 = facade.requestCustomerInfo("1234431");
  //    printModel(customer2);
  //  }
  //
  //  public static void putCustomer() throws Exception {
  //    CustomerApiFacade facade = buildIcFacade().getCustomerFacade();
  //    Customer costumerInfo = CustomerBuilder.buildTestCustomerBuilder().buildModel();
  //    costumerInfo.setVatNumber(null);
  //    costumerInfo.setExternalId(null);
  //    costumerInfo.setName("Brand new Name");
  //    Customer customer = facade.updateCustomerInfo(costumerInfo.toEnumMap(), "1234431");
  //    printModel(customer);
  //  }
  //
  //  public static void postCustomerAttributes() throws Exception {
  //    CustomerApiFacade facade = buildIcFacade().getCustomerFacade();
  //    Map<String, String> map = new TreeMap<>();
  //    map.put("a", "12");
  //    map.put("b", "string");
  //    map.put("w", "bcn");
  //
  //    Map<String, String> returnedMap = facade.setCustomerAttributes("1234431", map);
  //    System.out.println(returnedMap);
  //  }
  //
  //  public static void getCustomerAttributes() throws Exception {
  //    CustomerApiFacade facade = buildIcFacade().getCustomerFacade();
  //
  //    Map<String, String> returnedMap = facade.requestCustomerAttributes("1234431");
  //    System.out.println(returnedMap);
  //  }
  //
  //  public static void postDebt() throws Exception {
  //    DebtApiFacade debtFacade = buildIcFacade().getDebtFacade();
  //    Debt debt = DebtBuilder.buildTestDebtBuilder().buildModel();
  //    debt.setCustomerId("0d3987e3-a6df-422c-8722-3fde26eec9a8");
  //    debt.setGrossTotal(777.0);
  //    debt.setNumber("5");
  //    Debt returnedDebt = debtFacade.registerNewDebt(debt);
  //    printModel(returnedDebt);
  //  }
  //
  //  public static void getDebt() throws Exception {
  //    DebtApiFacade debtFacade = buildIcFacade().getDebtFacade();
  //    Debt returnedDebt = debtFacade.requestDebtInfo("39b95a13-a295-405b-973f-9722bea87014");
  //    printModel(returnedDebt);
  //  }
  //
  //  public static void getCustomerDebts() throws Exception {
  //    CustomerApiFacade facade = buildIcFacade().getCustomerFacade();
  //
  //    List<Debt> returned = facade.requestCustomerDebts("0d3987e3-a6df-422c-8722-3fde26eec9a8");
  //    printModel(returned);
  //    System.out.println(returned.size());
  //  }

  //  {"code":409,"gid":"0d3987e3-a6df-422c-8722-3fde26eec9a8","message":"Customer already
  // registered."}

  // 8ff7ee09-2a39-44b1-8928-ed452f58291d
  // "1fb0c683-bedc-45be-a88a-ff76da7bf650"
  // ee221916-0428-4b5c-af43-8e542d2c5a98
  // customerid 0d3987e3-a6df-422c-8722-3fde26eec9a8

}
