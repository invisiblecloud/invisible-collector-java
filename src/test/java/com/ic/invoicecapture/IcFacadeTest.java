package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.connection.builders.IThrowingBuilder;
import com.ic.invoicecapture.connection.response.validators.IValidator;
import com.ic.invoicecapture.connection.response.validators.ValidatorBuilder;
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

public class IcFacadeTest {

  private ApiRequestFacade apiMock;
  private JsonModelFacade jsonMock;
  private IcFacade icFacade;
  private InputStream inputStream;
  private ValidatorBuilder validatorBuilderMock;
  private IValidator validatorMock;


  @BeforeEach
  public void init() {
    this.apiMock = EasyMock.createNiceMock(ApiRequestFacade.class);
    this.jsonMock = EasyMock.createNiceMock(JsonModelFacade.class);
    this.validatorBuilderMock = EasyMock.createNiceMock(ValidatorBuilder.class);
    this.icFacade = new IcFacade(this.apiMock, this.jsonMock, this.validatorBuilderMock);
    this.inputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
    this.validatorMock = EasyMock.createNiceMock(IValidator.class);

    EasyMock.expect(validatorBuilderMock.build()).andReturn(this.validatorMock);
    EasyMock.expect(validatorBuilderMock.clone()).andReturn(this.validatorBuilderMock);

    EasyMock.replay(this.validatorBuilderMock);
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

  private void assertRequestGuts(IThrowingBuilder<Company, Void> method) throws IcException {
    CompanyBuilder companyBuilder = this.initJsonParserMock();
    EasyMock.replay(this.jsonMock);
    EasyMock.replay(this.validatorMock);
    EasyMock.replay(this.apiMock);
    Company correctCompany = companyBuilder.buildCompany();
    Company returnedCompany = method.build(null);

    Assertions.assertEquals(correctCompany, returnedCompany);
  }
  //
  // @Test
  // public void requestCompanyInfo_correctness() throws IcException {
  //
  // EasyMock
  // .expect(this.apiMock.getRequest(EasyMock.isA(IValidator.class), EasyMock.isA(String.class)))
  // .andReturn(this.inputStream);
  //
  // assertRequestGuts((unused) -> this.icFacade.requestCompanyInfo());
  // }
  //
  //
  //
  // @Test
  // public void updateCompanyInfo_correctness() throws IcException {
  //
  // EasyMock.expect(this.apiMock.putRequest(EasyMock.isA(IValidator.class), EasyMock.anyString(),
  // EasyMock.anyString())).andReturn(this.inputStream);
  //
  // assertRequestGuts(
  // (unused) -> this.icFacade.updateCompanyInfo(EasyMock.isA(ICompanyUpdate.class)));
  // }

}
