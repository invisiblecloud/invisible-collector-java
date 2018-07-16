package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.ICompanyUpdate;
import com.ic.invoicecapture.model.builder.CompanyBuilder;
import com.ic.invoicecapture.model.json.JsonModelFacade;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IcFacadeTest {

  private ApiRequestFacade apiMock;
  private JsonModelFacade jsonMock;
  private IcFacade icFacade;
  private InputStream inputStream;


  @BeforeEach
  public void init() {
    this.apiMock = EasyMock.createNiceMock(ApiRequestFacade.class);
    this.jsonMock = EasyMock.createNiceMock(JsonModelFacade.class);
    this.icFacade = new IcFacade(this.apiMock, this.jsonMock);
    this.inputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
  }
  
  private CompanyBuilder initJsonParserMock() throws IcException {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    Company correctCompany = companyBuilder.buildCompany();
    String companyJson = companyBuilder.buildJsonObject().toString();
    EasyMock.expect(this.jsonMock.parseStringStream(EasyMock.isA(InputStream.class),
        EasyMock.eq(Company.class))).andReturn(correctCompany);
    EasyMock.expect(this.jsonMock.toJson(EasyMock.isA(ICompanyUpdate.class)))
        .andReturn(companyJson);
    
    return companyBuilder;
  }

  @Test
  public void requestCompanyInfo_correctness() throws IcException {

    EasyMock.expect(this.apiMock.getRequest(EasyMock.isA(String.class)))
        .andReturn(this.inputStream);
    EasyMock.replay(this.apiMock);

    CompanyBuilder companyBuilder = this.initJsonParserMock();
    EasyMock.replay(this.jsonMock);
    Company correctCompany = companyBuilder.buildCompany();
    Company returnedCompany = this.icFacade.requestCompanyInfo();

    Assertions.assertEquals(correctCompany, returnedCompany);
  }

  @Test
  public void updateCompanyInfo_correctness() throws IcException {
    
    EasyMock.expect(this.apiMock.putRequest(EasyMock.anyString(), EasyMock.anyString()))
        .andReturn(this.inputStream);
    EasyMock.replay(this.apiMock);
    
    CompanyBuilder companyBuilder = this.initJsonParserMock();
    EasyMock.replay(this.jsonMock);
    Company correctCompany = companyBuilder.buildCompany();
    Company returnedCompany = this.icFacade.updateCompanyInfo(correctCompany);

    Assertions.assertEquals(correctCompany, returnedCompany);
  }

}
