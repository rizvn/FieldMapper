package com.rizvn.fieldmapper.domain;

import com.rizvn.fieldmapper.annotation.Column;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Riz
 */
public class User{
  @Column
  public String firstName;

  @Column
  public String lastName;

  @Column
  public String email;

  @Column(typeHandler = TimestampToJodaDateTime.class)
  public DateTime lastUpdated;
}
