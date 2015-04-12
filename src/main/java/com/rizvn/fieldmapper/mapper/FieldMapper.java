package com.rizvn.fieldmapper.mapper;

import com.rizvn.fieldmapper.annotation.Column;
import com.rizvn.fieldmapper.annotation.Table;
import com.rizvn.fieldmapper.exception.FieldMapperException;
import com.rizvn.fieldmapper.typehandler.TypeHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Riz
 */
public class FieldMapper{

  final private static Map<Class<?>, Map<String, Pair<Field, Column>>> metaDataCache = new HashMap<>();

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

  public static Map<String, Pair<Field, Column>> getMetaData(Class<?> aClass){

    try {
      if(metaDataCache.containsKey(aClass)){
        return metaDataCache.get(aClass);
      }

      Map<String, Pair<Field, Column>> metaData = new HashMap<>();
      for (Field field : aClass.getDeclaredFields()) {
        for (Annotation annotation : field.getDeclaredAnnotations()) {
          if (annotation instanceof Column) {
            Column colAnnotation = (Column) annotation;
            String fieldName = field.getName();
            //use the field value on annotation if provided for field name
            if (!colAnnotation.value().equals("")) {
              fieldName = colAnnotation.value();
            }

            metaData.put(fieldName, Pair.of(field, colAnnotation));
          }
        }
      }
      metaDataCache.put(aClass,metaData);
      return metaData;
    }
    catch (Exception ex){
        throw new RuntimeException(ex);
    }
  }


  public static void updateFieldValues(Map<String, Object> fromMap, Object object){
    Map<String, Pair<Field, Column>> metaData = getMetaData(object.getClass());

    try
    {
      for(Map.Entry<String, Pair<Field, Column>> fieldEntry: metaData.entrySet()){
        String fieldName = fieldEntry.getKey();
        Field field = fieldEntry.getValue().getLeft();
        Column colAnnotation = fieldEntry.getValue().getRight();

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
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }


  public static String deleteQuery(Object object){
    try {
      //get table name
      String tableName = object.getClass().getSimpleName();
      for(Table tableAnnotation : object.getClass().getDeclaredAnnotationsByType(Table.class)){
        if(!tableAnnotation.value().equals("")){
          tableName = tableAnnotation.value();
        }
      }

      //get id field name
      String idFieldName = null;
      for (Field field : object.getClass().getDeclaredFields()) {
        for (Annotation annotation : field.getDeclaredAnnotations()) { //get annotations on field
          if (annotation instanceof Column) {                          //if annotation is a column annotation
            Column colAnnotation = (Column) annotation;                //get the col annotation
            if(colAnnotation.id()){
              idFieldName =  colAnnotation.value().equals("")? field.getName() : colAnnotation.value();
            }
          }
        }
      }

      if(idFieldName == null) {
        throw new FieldMapperException("Unable to find id field for class: "+ object.getClass().getCanonicalName());
      }
      return String.format("DELETE FROM %s WHERE %s = ?", tableName, idFieldName);
    }
    catch (Exception ex){
      throw new RuntimeException(ex);
    }
  }

  public static String updateQuery(Object object){
    try {

      //get table name
      String tableName = object.getClass().getSimpleName();
      for(Table tableAnnotation : object.getClass().getDeclaredAnnotationsByType(Table.class)){
        if(!tableAnnotation.value().equals("")){
          tableName = tableAnnotation.value();
        }
      }

      List<String> columnNames  = new ArrayList<>();
      //get id field name
      String idFieldName = null;
      for (Field field : object.getClass().getDeclaredFields()) {
        for (Annotation annotation : field.getDeclaredAnnotations()) { //get annotations on field
          if (annotation instanceof Column) {                          //if annotation is a column annotation
            Column colAnnotation = (Column) annotation;                //get the col annotation
            if(colAnnotation.id()){
              idFieldName =  colAnnotation.value().equals("")? field.getName() : colAnnotation.value();
            }
            else {
              columnNames.add(colAnnotation.value().equals("")? field.getName() : colAnnotation.value());
            }
          }
        }
      }

      if(idFieldName == null) {
        throw new FieldMapperException("Unable to find id field for class: "+ object.getClass().getCanonicalName());
      }


      StringBuilder stringBuilder = new StringBuilder();
      boolean first = true;
      for(String name : columnNames){
        if(first){
          first = false;
        }
        else{
          stringBuilder.append(",");
        }
        stringBuilder.append(name + "=?");
      }

      return String.format("UPDATE %s set %s where %s=?", tableName, stringBuilder.toString(), idFieldName);
    }
    catch (Exception ex){
      throw new RuntimeException(ex);
    }
  }

  public static String insertQuery(Object object){
    try {

      //get table name
      String tableName = object.getClass().getSimpleName();
      for(Table tableAnnotation : object.getClass().getDeclaredAnnotationsByType(Table.class)){
        if(!tableAnnotation.value().equals("")){
          tableName = tableAnnotation.value();
        }
      }

      List<String> columnNames  = new ArrayList<>();
      //get id field name
      String idFieldName = null;
      for (Field field : object.getClass().getDeclaredFields()) {
        for (Annotation annotation : field.getDeclaredAnnotations()) { //get annotations on field
          if (annotation instanceof Column) {                          //if annotation is a column annotation
            Column colAnnotation = (Column) annotation;                //get the col annotation
            if(colAnnotation.id()){
              idFieldName =  colAnnotation.value().equals("")? field.getName() : colAnnotation.value();
            }
            else {
              columnNames.add(colAnnotation.value().equals("")? field.getName() : colAnnotation.value());
            }
          }
        }
      }

      if(idFieldName == null) {
        throw new FieldMapperException("Unable to find id field for class: "+ object.getClass().getCanonicalName());
      }


      StringBuilder colNames = new StringBuilder();
      StringBuilder colParams = new StringBuilder();

      colNames.append("(");
      colParams.append("(");

      boolean first = true;
      for(String name : columnNames){
        if(first){
          first = false;
        }
        else{
          colNames.append(",");
          colParams.append(",");
        }
        colNames.append(name);
        colParams.append("?");
      }
      colNames.append(")");
      colParams.append(")");

      return String.format("INSERT INTO %s %s values %s", tableName, colNames.toString(), colParams.toString());
    }
    catch (Exception ex){
      throw new FieldMapperException(ex);
    }
  }
}
