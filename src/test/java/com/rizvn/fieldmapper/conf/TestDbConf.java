package com.rizvn.fieldmapper.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * @author Riz
 */
@Configuration
public class TestDbConf {
  @Bean
  public DataSource dataSource(){
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setDriverClass(org.h2.Driver.class);
    dataSource.setUsername("sa");
    dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
    dataSource.setPassword("");

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    System.out.println("Creating tables in h2");
    jdbcTemplate.execute("drop table users if exists");
    jdbcTemplate.execute("create table users(id serial, firstName varchar(255), lastName varchar(255), email varchar(255), lastUpdated TIMESTAMP)");
    jdbcTemplate.update(
       "INSERT INTO users(firstName, lastName, email, lastUpdated) "
      +"VALUES (?,?,?, ?)", "Riz", "Van", "riz247@gmail.com", "2014-01-01 00:00:00");
    System.out.println("Finished creating tables in h2");
    return dataSource;
  }

  @Bean
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource());
    return namedParameterJdbcTemplate;
  }

  @Bean
  public JdbcTemplate jdbcTemplate(){
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
    return jdbcTemplate;
  }

  @Bean
  @Autowired
  public DataSourceTransactionManager transactionManager(DataSource dataSource){
    return new DataSourceTransactionManager(dataSource);
  }

}
