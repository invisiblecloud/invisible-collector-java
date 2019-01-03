package com.invisiblecollector.model;

import com.invisiblecollector.model.builder.CustomerBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomerTest {
  
  @Test
  public void equals_empty() {

    Customer customer1 = new Customer();
    Customer customer2 = new Customer();

    Assertions.assertEquals(customer1, customer2);

    customer1.setName("12321");
    Assertions.assertNotEquals(customer1, customer2);
  }
  
  @Test
  public void equals_seeded() {
    CustomerBuilder builder = CustomerBuilder.buildTestCustomerBuilder();
    Customer customer1 = builder.buildModel();
    Customer customer2 = builder.buildModel();
    
    Assertions.assertEquals(customer1, customer2);
    
    customer2.setCity("some city");
    Assertions.assertNotEquals(customer1, customer2);
  }
  
  @Test
  public void equals_identity() {
    CustomerBuilder builder = CustomerBuilder.buildTestCustomerBuilder();
    Customer customer = builder.buildModel();
    Assertions.assertEquals(customer, customer);
  }

  @Test
  public void hashCode_correctness() {
    CustomerBuilder builder = CustomerBuilder.buildTestCustomerBuilder();
    Customer customer1 = builder.buildModel();
    Customer customer2 = builder.buildModel();
    
    Assertions.assertEquals(customer1.hashCode(), customer2.hashCode());

    customer1.setName("some other name");
    Assertions.assertNotEquals(customer1.hashCode(), customer2.hashCode());
  }
  
  private static final String TEST_ID = "1234";
  private static final String TEST_EXTERNAL_ID = "098";
  
  @Test
  public void getRoutableId_correctOrder() {
    Customer customer = new Customer();
    customer.setGid(TEST_ID);
    customer.setExternalId(TEST_EXTERNAL_ID);

    Assertions.assertEquals(TEST_ID, customer.getRoutableId());
  }
  
  @Test
  public void getRoutableId_returnsExternalId() {
    Customer customer = new Customer();
    customer.setGid(null);
    customer.setExternalId(TEST_EXTERNAL_ID);

    Assertions.assertEquals(TEST_EXTERNAL_ID, customer.getRoutableId());
  }
  
  @Test
  public void getRoutableId_fail() {
    Customer customer = new Customer();
    customer.setGid("");

    Assertions.assertThrows(IllegalArgumentException.class, customer::getRoutableId);
  }
  
  
}
