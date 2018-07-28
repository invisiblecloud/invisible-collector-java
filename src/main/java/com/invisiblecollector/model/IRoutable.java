package com.invisiblecollector.model;

/**
 * Implementing models can be used to return an id, that can reference the model
 * in the remote database.
 * 
 * @author ros
 */
public interface IRoutable {
  /**
   * Get a valid routable id of the model. Can be used to reference the model
   * in the remote database for updates, deletions, etc.
   * 
   * @return the id
   */
  String getRoutableId();
}
