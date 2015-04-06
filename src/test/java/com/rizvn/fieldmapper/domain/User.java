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
  String firstName;

  @Column
  String lastName;

  @Column
  String email;

  @Column(typeHandler = TimestampToJodaDateTime.class)
  DateTime lastUpdated;
}
