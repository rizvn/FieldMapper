package com.rizvn.fieldmapper.domain;

import com.rizvn.fieldmapper.typehandler.TypeHandler;
import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * @author Riz
 */
public class TimestampToJodaDateTime implements TypeHandler {
  @Override
  public DateTime transform(Object src) {
    Timestamp timestamp = (Timestamp) src;
    return new DateTime(timestamp);
  }
}
