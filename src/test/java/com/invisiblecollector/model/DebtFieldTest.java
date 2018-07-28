package com.invisiblecollector.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DebtFieldTest {

  @Test
  public void assertValueIsValid_passFloatingPoint() {
    DebtField.NET_TOTAL.assertValueIsValid(0.4f);
    DebtField.NET_TOTAL.assertValueIsValid(0.4d);
    DebtField.NET_TOTAL.assertValueIsValid(13);
  }

  @Test
  public void assertValueIsValid_failFloatingPoint() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> DebtField.NET_TOTAL.assertValueIsValid("asd"));
  }

  @Test
  public void assertValueIsValid_passStringMap() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> DebtField.NET_TOTAL.assertValueIsValid("asd"));
  }

  @Test
  public void assertValueIsValid_passItemEmpty() {
    DebtField.ITEMS.assertValueIsValid(null);
    DebtField.ITEMS.assertValueIsValid(new ArrayList<Object>());
    DebtField.ITEMS.assertValueIsValid(new Map[0]);
  }

  private static final Map<ItemField, Object> TEST_VALID_ITEM;
  private static final Map<ItemField, Object> TEST_ANOTHER_VALID_ITEM;
  
  static {
    Map<ItemField, Object> map = new EnumMap<>(ItemField.class);
    map.put(ItemField.NAME, "test name");
    map.put(ItemField.PRICE, 2.3);
    map.put(ItemField.QUANTITY, 5f);
    TEST_VALID_ITEM = Collections.unmodifiableMap(new EnumMap<>(map));
    map.put(ItemField.NAME, "a new name");
    TEST_ANOTHER_VALID_ITEM = Collections.unmodifiableMap(new EnumMap<>(map));
  }

  @Test
  public void assertValueIsValid_passValidItems() {
    @SuppressWarnings("unchecked")
    Map<ItemField, Object>[] itemArray = new Map[] {TEST_VALID_ITEM, TEST_ANOTHER_VALID_ITEM};
    List<Map<ItemField, Object>> itemList = Arrays.asList(itemArray);
    DebtField.ITEMS.assertValueIsValid(itemList);
    DebtField.ITEMS.assertValueIsValid(itemArray);
  }

  @Test
  public void assertValueIsValid_failNull() {
    @SuppressWarnings("unchecked")
    Map<ItemField, Object>[] itemArray = new Map[] {TEST_VALID_ITEM, null};
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> DebtField.ITEMS.assertValueIsValid(itemArray));
  }

  @Test
  public void assertValueIsValid_failInvalidType() {
    Object[] itemArray = new Object[] {TEST_VALID_ITEM, (Integer) 123};
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> DebtField.ITEMS.assertValueIsValid(itemArray));
  }
  
  @Test
  public void assertValueIsValid_failInvalidItemMapType() {
    Map<ItemField, Object> invalidItem = new EnumMap<>(TEST_ANOTHER_VALID_ITEM);
    invalidItem.put(ItemField.VAT, new ArrayList<Object>());
    
    Object[] itemArray = new Object[] {invalidItem};
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> DebtField.ITEMS.assertValueIsValid(itemArray));
  }


}
