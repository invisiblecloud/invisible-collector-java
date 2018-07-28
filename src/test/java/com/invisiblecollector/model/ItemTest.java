package com.invisiblecollector.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.invisiblecollector.model.builder.ItemBuilder;

public class ItemTest {
  @Test
  public void equals_empty() {

    Item item1 = new Item();
    Item item2 = new Item();

    Assertions.assertEquals(item1, item2);

    item1.setDescription("a wholly new description");
    Assertions.assertNotEquals(item1, item2);
  }
  
  @Test
  public void equals_null() {

    Item item = new Item();

    Assertions.assertNotEquals(item, null);
  }
  
  @Test
  public void equals_seeded() {
    ItemBuilder builder = ItemBuilder.buildTestItemBuilder();
    Item item1 = builder.buildModel();
    Item item2 = builder.buildModel();
    
    Assertions.assertEquals(item1, item2);
    
    item2.setDescription("a wholly new description");
    Assertions.assertNotEquals(item1, item2);
  }
  
  @Test
  public void equals_identity() {
    ItemBuilder builder = ItemBuilder.buildTestItemBuilder();
    Item item = builder.buildModel();
    Assertions.assertEquals(item, item);
  }

  @Test
  public void hashCode_correctness() {
    Item item1 = new Item();
    Item item2 = new Item();

    Assertions.assertEquals(item1.hashCode(), item2.hashCode());

    item1.setDescription("a wholly new description");
    Assertions.assertNotEquals(item1.hashCode(), item2.hashCode());
  }
}
