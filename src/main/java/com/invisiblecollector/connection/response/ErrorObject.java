package com.invisiblecollector.connection.response;

public class ErrorObject {
  private Integer code;
  private String message;
  private String gid;

  public Integer getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public String getGid() {
    return gid;
  }

  public boolean hasGid() {
    return gid != null;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setGid(String gid) {
    this.gid = gid;
  }
}
