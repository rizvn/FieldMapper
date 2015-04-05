package com.rizvn.fieldmapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Riz
 */
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface Column {

  String value() default "";

  Class<?> typeHandler() default Object.class;



}
