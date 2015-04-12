package com.rizvn.fieldmapper.exception;

import com.rizvn.fieldmapper.mapper.FieldMapper;

/**
 * @author Riz
 */
public class FieldMapperException extends RuntimeException {
  public FieldMapperException(String message, Exception exception){
    super(message, exception);
  }

  public FieldMapperException(String message){
    super(message);
  }

  public FieldMapperException(Exception exception){
    super(exception);
  }
}
