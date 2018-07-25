package com.ic.invisiblecollector.model;

/**
 * Implementing models can be used to store the remote database id,
 * which can facilitate library use.
 * 
 * @author ros
 */
public interface IRoutable {
  /**
   * Get the id of the model. Can be used to reference the model
   * in the remote database for updates, deletions, etc.
   * 
   * @return the id
   */
  String getId();
}
