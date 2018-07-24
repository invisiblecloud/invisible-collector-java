package com.ic.invoicecapture.exceptions;

public class IcConflictingException extends IcException {

  private static final long serialVersionUID = 1L;
  private String gid;

  public IcConflictingException(String msg, String gid) {
    super(msg + ", conflicting object's gid: " + gid);
    this.gid = gid;
  }

  public String getGid() {
    return gid;
  }  
}
