package com.rizvn.fieldmapper.mapper;

import com.rizvn.fieldmapper.annotation.Column;
import com.rizvn.fieldmapper.typehandler.TypeHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Riz
 */
public class FieldMapper{

  public static <T> T resultSetToClass(ResultSet fromResultSet, Class<?> toClass) {
    //convert result set to map

    //mapToClass(map, toClass)
    return null;
  }

  public static <T> T mapListToObjectList(List<Map<String, Object>> mapList, Class<?> toClass){
    List<Object> results = new ArrayList<>();
    for(Map<String, Object> map : mapList){
      results.add(mapToClass(map, toClass));
    }
    return (T) results;
  };


  public static <T> T mapToClass(Map<String, Object> fromMap, Class<?> toClass){
     try{
       Object instance = toClass.newInstance();
       updateFieldValues(fromMap, instance);
       return (T) instance;
     }
     catch (Exception ex){
       throw new RuntimeException("Unable to create class of type "+ toClass + " from map");
     }
  };

  public static class Tuple{
    public Field field;
    public Column column;
  }

  public static List<Tuple> findFieldsAnnotatedByColumn(Object object){
    List<Tuple> tuples = new ArrayList<>();

    try {
      for (Field field : object.getClass().getDeclaredFields()) {
        for (Annotation annotation : field.getDeclaredAnnotations()) { //get annotations on field
          if (annotation instanceof Column) {                          //if annotation is a column annotation
            Column colAnnotation = (Column) annotation;                //get the col annotation
            Tuple tuple = new Tuple();
            tuple.column = colAnnotation;
            tuple.field = field;
            tuples.add(tuple);
          }
        }
      }
    }
    catch (Exception ex){
      throw new RuntimeException(ex);
    }
    return tuples;
  }

  public static void updateFieldValues(Map<String, Object> fromMap, Object object){
    try {
      for (Field field : object.getClass().getDeclaredFields()) {
        for (Annotation annotation : field.getDeclaredAnnotations()) { //get annotations on field
          if (annotation instanceof Column) {                          //if annotation is a column annotation
            Column colAnnotation = (Column) annotation;                //get the col annotation

            String fieldName = field.getName();

            //use the field value on annotation if provided for field name
            if (!colAnnotation.value().equals("")) {
              fieldName = colAnnotation.value();
            }

            Class<?> targetType = field.getType();
            Object value = fromMap.get(fieldName);  //get value from map
            Boolean originalAccessibility = field.isAccessible();
            field.setAccessible(true);

            if(value == null){ //handle null value
              field.set(object, null);
            }
            else if(colAnnotation.typeHandler() != Object.class){  //transform type using handler if specified
              Class<?> handlerClass = colAnnotation.typeHandler();
              TypeHandler typeHandler = (TypeHandler) handlerClass.newInstance();
              field.set(object, typeHandler.transform(value)); //transform value
            }
            else if(targetType.isInstance(value)) {   //field type matches type in map
              field.set(object, value);
            }
            field.setAccessible(originalAccessibility);
          }
        }
      }
    }
    catch (Exception ex){
      throw new RuntimeException(ex);
    }
  }
}
