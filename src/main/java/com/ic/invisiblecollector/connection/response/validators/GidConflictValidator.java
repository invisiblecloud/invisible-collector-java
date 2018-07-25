package com.ic.invisiblecollector.connection.response.validators;

import com.ic.invisiblecollector.exceptions.IcConflictingException;
import com.ic.invisiblecollector.exceptions.IcException;

/**
 * Validation Can throw an exception of type {@code IcConflictingException } which contains the
 * conflicting customer's gid.
 * Immutable class.
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
