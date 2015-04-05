package com.rizvn.fieldmapper.mapper;

import com.rizvn.fieldmapper.domain.User;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Riz
 */
public class FieldMapperTest {

   @Test
   public void mapToClassTest(){
     HashMap<String, Object> map = new HashMap<>();
     map.put("firstName", "Riz");
     map.put("lastUpdated", new Timestamp(new Date().getTime()));

     User user = FieldMapper.mapToClass(map, User.class);
     Assert.assertTrue(user instanceof  User);
   }
}
