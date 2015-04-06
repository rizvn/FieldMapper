package com.rizvn.fieldmapper.map;

import com.rizvn.fieldmapper.conf.TestDbConf;
import com.rizvn.fieldmapper.domain.TimestampToJodaDateTime;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Riz
 */
@ContextConfiguration(classes = TestDbConf.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ResultMapIntegrationTest {

  @Resource
  JdbcTemplate jdbcTemplate;

  static enum Users{
    FIRSTNAME, LASTUPDATED
  }

  @Test
  public void testResultMap(){
    List<ResultMap> results = ResultMap.create(jdbcTemplate.queryForList("SELECT * FROM USERS"));
    String firstName = results.get(0).getTyped("FIRSTNAME");
    Assert.assertTrue(firstName.equals("Riz"));
  }

  @Test
  public void testResultMapWithEnum(){
    List<ResultMap> results = ResultMap.create(jdbcTemplate.queryForList("SELECT * FROM USERS"));
    String firstName = results.get(0).getTyped(Users.FIRSTNAME);
    Assert.assertTrue(firstName.equals("Riz"));
  }

  @Test
  public void testResultMapWithTypeHandler(){
    List<ResultMap> results = ResultMap.create(jdbcTemplate.queryForList("SELECT * FROM USERS"));
    DateTime lastUpdated = results.get(0).getTyped(Users.LASTUPDATED, new TimestampToJodaDateTime());
    Assert.assertNotNull(lastUpdated);
  }



}
