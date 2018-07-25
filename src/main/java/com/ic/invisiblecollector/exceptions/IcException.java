package com.ic.invisiblecollector.exceptions;

/**
 * A General library exception. Usually used to wrap 
 * around other exceptions caused by 3rd party libraries.
 * 
 * @author ros
 */
public class IcException extends Exception {

  private static final long serialVersionUID = 1L;

  public IcException() {
    super();
  }

  public IcException(String msg) {
    super(msg);
  }

  public IcException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public IcException(Throwable e) {
    super(e);
  }
}
