package com.ic.invisiblecollector.model;

/**
 * Implementing models can be used to store the remote database id or external id
 * which can facilitate library use.
 * 
 * @author ros
 */
public interface IInternallyRoutable extends IRoutable {
  @Override
  String getId();
  
  /**
   * Get the external id of the model. The external id can be for example the id of the 
   * corresponding model in the local database.
   * 
   * <p>Can be used to reference the model in the remote database 
   * (for updates, deletions, etc) if the id of the model is null. 
   * 
   * @return the external id 
   */
  String getExternalId();
}
