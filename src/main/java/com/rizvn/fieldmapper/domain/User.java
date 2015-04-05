package com.rizvn.fieldmapper.domain;

import com.rizvn.fieldmapper.annotation.Column;
import com.rizvn.fieldmapper.types.TimestampToJodaDateTime;
import org.joda.time.DateTime;

/**
 * @author Riz
 */
public class User {
  @Column
  String firstName;

  @Column
  String lastName;

  @Column
  String email;

  @Column(typeHandler = TimestampToJodaDateTime.class)
  DateTime lastUpdated;
}
