package com.rizvn.fieldmapper.query;

import com.rizvn.fieldmapper.annotation.Column;
import com.rizvn.fieldmapper.annotation.Table;
import org.junit.Test;

/**
 * @author Riz
 */
public class CrudTest {

  @Table
  public static class Customer implements Crud{
    @Column(id=true)
    Long customerId;

    @Column
    String name;

    @Column
    String address;
  }

  @Test
  public void deleteQueryTest(){
    Customer customer = new Customer();
    customer.customerId = 1L;
    customer.name = "Riz";
    customer.address = "some street";
    String deleteQuery = customer.deleteQuery();
    System.out.println(deleteQuery);
  }

  @Test
  public void updateQueryTest(){
    Customer customer = new Customer();
    customer.customerId = 1L;
    customer.name = "Riz";
    customer.address = "some street";
    String updateQuery = customer.updateQuery();
    System.out.println(updateQuery);
  }

  @Test
  public void insertQueryTest(){
    Customer customer = new Customer();
    customer.customerId = 1L;
    customer.name = "Riz";
    customer.address = "some street";
    String insertQuery = customer.insertQuery();
    System.out.println(insertQuery);
  }
}
