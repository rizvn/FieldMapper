package com.rizvn.fieldmapper.types;

import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * @author Riz
 */
public class TimestampToJodaDateTime implements TypeHandler{

  @Override
  public <T> T transform(Object src) {
    Timestamp timestamp = (Timestamp) src;
    return (T) new DateTime(timestamp);
  }
}
