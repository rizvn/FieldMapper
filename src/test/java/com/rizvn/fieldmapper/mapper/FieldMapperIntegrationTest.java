package com.rizvn.fieldmapper.mapper;

import com.rizvn.fieldmapper.conf.TestDbConf;
import com.rizvn.fieldmapper.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Riz
 */
@ContextConfiguration(classes = TestDbConf.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class FieldMapperIntegrationTest {

  @Resource
  JdbcTemplate jdbcTemplate;

  @Test
  public void testMapListToObjectList(){
    List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * FROM USERS");
    List<User> users = FieldMapper.mapListToObjectList(results, User.class);
    Assert.assertFalse(users.isEmpty());
  }
}
