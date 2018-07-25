package com.ic.invisiblecollector.exceptions;

/**
 * Used to indicate that a model already exists in the remote database 
 * that has already taken an unique constrained attribute or field.
 * 
 * @author ros
 */
public class IcConflictingException extends IcException {

  private static final long serialVersionUID = 1L;
  private String gid;

  public IcConflictingException(String msg, String gid) {
    super(msg + ", conflicting object's gid: " + gid);
    this.gid = gid;
  }

  /**
   * Get the id of the conflicting model in the remote database.
   * 
   * @return the conflicting model's id
   */
  public String getGid() {
    return gid;
  }  
}
