package com.rizvn.fieldmapper.mapper;

import com.rizvn.fieldmapper.annotation.Column;
import com.rizvn.fieldmapper.annotation.Table;
import com.rizvn.fieldmapper.domain.User;
import com.rizvn.fieldmapper.query.Crud;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Riz
 */
public class FieldMapperTest {
  @Table
  public static class Customer {
    @Column(id=true)
    Long customerId;

    @Column
    String name;

    @Column
    String address;
  }

  @Test
  public void mapToClassTest(){
    HashMap<String, Object> map = new HashMap<>();
    map.put("firstName", "Riz");
    map.put("lastUpdated", new Timestamp(new Date().getTime()));

    User user = FieldMapper.mapToClass(map, User.class);
    Assert.assertTrue(user instanceof  User);
    Assert.assertEquals(user.firstName, "Riz");
  }

  @Test
  public void getMetaDataTest(){
    Map<String, Pair<Field, Column>> metaData = FieldMapper.getMetaData(User.class);
    FieldMapper.getMetaData(User.class);
    Assert.assertNotNull(metaData);
  }

  @Test
  public void deleteQueryTest(){
    Customer customer = new Customer();
    customer.customerId = 1L;
    customer.name = "Riz";
    customer.address = "some street";
    String deleteQuery = FieldMapper.deleteQuery(customer);
    System.out.println(deleteQuery);
  }

  @Test
  public void updateQueryTest(){
    Customer customer = new Customer();
    customer.customerId = 1L;
    customer.name = "Riz";
    customer.address = "some street";
    String updateQuery = FieldMapper.updateQuery(customer);
    System.out.println(updateQuery);
  }

  @Test
  public void insertQueryTest(){
    Customer customer = new Customer();
    customer.customerId = 1L;
    customer.name = "Riz";
    customer.address = "some street";
    String insertQuery = FieldMapper.insertQuery(customer);
    System.out.println(insertQuery);
  }
}
