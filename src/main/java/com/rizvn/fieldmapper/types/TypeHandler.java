package com.rizvn.fieldmapper.types;

/**
 * @author Riz
 */
public interface TypeHandler {

  public <TargetType> TargetType transform(Object src);
}
