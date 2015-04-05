package com.rizvn.fieldmapper.typehandler;

/**
 * @author Riz
 */
public interface TypeHandler {

  public <TargetType> TargetType transform(Object src);
}
