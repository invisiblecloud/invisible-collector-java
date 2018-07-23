package com.ic.invoicecapture.model;

public interface IInternallyRoutable extends IRoutable {
  @Override
  String getId();
  
  String getExternalId();
}
