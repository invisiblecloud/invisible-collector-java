package com.invisiblecollector.model;

import com.invisiblecollector.model.builder.DebtBuilder;
import com.invisiblecollector.model.builder.ItemBuilder;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class DebtTest {
  @Test
  public void equals_empty() {

    Debt debt1 = new Debt();
    Debt debt2 = new Debt();

    Assertions.assertEquals(debt1, debt2);

    debt1.setCustomerId("909090909");
    Assertions.assertNotEquals(debt1, debt2);
  }

  @Test
  public void equals_null() {

    Debt debt = new Debt();

    Assertions.assertNotEquals(debt, null);
  }

  @Test
  public void equals_seeded() {
    DebtBuilder builder = DebtBuilder.buildTestDebtBuilder();
    Debt debt1 = builder.buildModel();
    Debt debt2 = builder.buildModel();

    Assertions.assertEquals(debt1, debt2);

    debt2.setCustomerId("909090909");
    Assertions.assertNotEquals(debt1, debt2);
  }

  @Test
  public void equals_identity() {
    DebtBuilder builder = DebtBuilder.buildTestDebtBuilder();
    Debt debt = builder.buildModel();
    Assertions.assertEquals(debt, debt);
  }

  @Test
  public void hashCode_correctness() {
    Debt debt1 = new Debt();
    Debt debt2 = new Debt();

    Assertions.assertEquals(debt1.hashCode(), debt2.hashCode());

    debt1.setCustomerId("909090909");
    Assertions.assertNotEquals(debt1.hashCode(), debt2.hashCode());
  }

  @Test
  public void addAttribute_correctness() {
    Map<String, String> actual = new HashMap<>();
    actual.put("1", "2");

    Debt debt = new Debt();
    debt.addAttribute("1", "2");

    Assertions.assertEquals(actual, debt.getAttributes());

    Map<String, String> actual2 = new HashMap<>(actual);
    actual.remove("1");
    Assertions.assertEquals(actual.size(), 0);

    Assertions.assertEquals(actual2, debt.getAttributes());
  }

  @Test
  public void setAttributes_correctness() {
    Debt debt = new Debt();
    Assertions.assertEquals(new HashMap<>(), debt.getAttributes());

    Map<String, String> actual = new HashMap<>();
    actual.put("1", "2");

    debt.setAttributes(actual);

    Assertions.assertEquals(actual, debt.getAttributes());

    Map<String, String> actual2 = new HashMap<>(actual);
    actual.remove("1");
    Assertions.assertEquals(actual.size(), 0);

    Assertions.assertEquals(actual2, debt.getAttributes());
  }

  @Test
  public void setItems_correctness() {

    Debt debt = new Debt();
    Assertions.assertEquals(new ArrayList<>(), debt.getItems());

    Item item = ItemBuilder.buildTestItemBuilder().buildModel();
    List<Item> expected = new ArrayList<>();
    expected.add(item);
    List<Item> expected2 = new ArrayList<>();
    expected2.add(item.clone());

    debt.setItems(expected);

    Assertions.assertEquals(expected, debt.getItems());

    item.setName("whole new Name MNSWEQWE123");
    Assertions.assertEquals(expected2, debt.getItems());

    expected.remove(item);
    Assertions.assertEquals(expected.size(), 0);

    Assertions.assertEquals(expected2, debt.getItems());
  }

  @Test
  public void addItems_correctness() {
    Item item = ItemBuilder.buildTestItemBuilder().buildModel();
    List<Item> expected = new ArrayList<>();
    expected.add(item);
    List<Item> expected2 = new ArrayList<>();
    expected2.add(item.clone());

    Debt debt = new Debt();
    debt.addItem(item);

    Assertions.assertEquals(expected, debt.getItems());

    item.setName("whole new Name MNSWEQWE123");
    Assertions.assertEquals(expected2, debt.getItems());

    expected.remove(item);
    Assertions.assertEquals(expected.size(), 0);

    Assertions.assertEquals(expected2, debt.getItems());
  }

  @Test
  public void setDate_order() {
    final String errorMsg = "dueDate must come after the debt date.";

    Date firstDate = new Date(1, 1, 1);
    Date midDate = new Date(2, 1, 1);
    Date lateDate = new Date(3, 1, 1);

    Debt debt = new Debt();
    // ok
    debt.setDate(midDate);
    debt.setDueDate(lateDate);

    IllegalArgumentException e =
        Assertions.assertThrows(IllegalArgumentException.class, () -> debt.setDueDate(firstDate));
    MatcherAssert.assertThat(e.getMessage(), CoreMatchers.containsString(errorMsg));

    Debt debt2 = new Debt();
    debt2.setDueDate(firstDate);
    IllegalArgumentException e2 =
        Assertions.assertThrows(IllegalArgumentException.class, () -> debt2.setDate(midDate));
    MatcherAssert.assertThat(e2.getMessage(), CoreMatchers.containsString(errorMsg));
  }

}
