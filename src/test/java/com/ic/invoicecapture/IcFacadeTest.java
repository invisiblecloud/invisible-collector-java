package com.ic.invoicecapture;

import com.ic.invoicecapture.connection.ApiRequestFacade;
import com.ic.invoicecapture.exceptions.IcException;
import com.ic.invoicecapture.model.Company;
import com.ic.invoicecapture.model.builder.CompanyBuilder;
import com.ic.invoicecapture.model.json.JsonModelFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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

  @Test
  public void requestCompanyInfo_correctness() throws IcException, IOException, URISyntaxException {
    CompanyBuilder companyBuilder = CompanyBuilder.buildTestCompanyBuilder();
    Company correctCompany = companyBuilder.buildCompany();

    EasyMock.expect(this.apiMock.getRequest(EasyMock.isA(String.class)))
        .andReturn(this.inputStream);
    EasyMock.expect(this.jsonMock.parseStringStream(EasyMock.isA(InputStream.class),
        EasyMock.eq(Company.class))).andReturn(correctCompany);

    EasyMock.replay(this.apiMock);
    EasyMock.replay(this.jsonMock);

    Company returnedCompany = this.icFacade.requestCompanyInfo();

    Assertions.assertEquals(correctCompany, returnedCompany);
  }

}
