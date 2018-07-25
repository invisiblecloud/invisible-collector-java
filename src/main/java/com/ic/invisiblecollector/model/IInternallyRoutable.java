package com.ic.invisiblecollector.model;

public interface IInternallyRoutable extends IRoutable {
  @Override
  String getId();
  
  String getExternalId();
}
