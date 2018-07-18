package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.exceptions.IcConflictingException;
import com.ic.invoicecapture.exceptions.IcException;

/**
 * Validation Can throw an exception of type {@code IcConflictingException } which contains the
 * conflicting customer's gid.
 * 
 * @author ros
 *
 */
public class GidConflictValidator extends SpecificStatusCodeValidator {
  public GidConflictValidator(String msg) {
    super(409, msg);
  }

  @Override
  protected void throwJsonException(ServerErrorFacade serverErrorFacade) throws IcException {
    String gid = serverErrorFacade.getErrorMember("gid");
    throw new IcConflictingException(this.exceptionMsg, gid);
  }

}