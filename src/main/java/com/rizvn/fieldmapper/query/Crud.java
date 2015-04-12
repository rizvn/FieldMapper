package com.rizvn.fieldmapper.query;

import com.rizvn.fieldmapper.annotation.Column;
import com.rizvn.fieldmapper.annotation.Table;
import com.rizvn.fieldmapper.exception.FieldMapperException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Riz
 */
public interface Crud {
  static HashMap<String, String> cache = new HashMap<>();

  default String deleteQuery(){
    String key = this.getClass().getCanonicalName() +"-deleteQuery";
    if(!cache.containsKey(key)) {
      try {
        //get table name
        String tableName = this.getClass().getSimpleName();
        for (Table tableAnnotation : this.getClass().getDeclaredAnnotationsByType(Table.class)) {
          if (!tableAnnotation.value().equals("")) {
            tableName = tableAnnotation.value();
          }
        }

        //get id field name
        String idFieldName = null;
        for (Field field : this.getClass().getDeclaredFields()) {
          for (Annotation annotation : field.getDeclaredAnnotations()) { //get annotations on field
            if (annotation instanceof Column) {                          //if annotation is a column annotation
              Column colAnnotation = (Column) annotation;                //get the col annotation
              if (colAnnotation.id()) {
                idFieldName = colAnnotation.value().equals("") ? field.getName() : colAnnotation.value();
              }
            }
          }
        }

        if (idFieldName == null) {
          throw new FieldMapperException("Unable to find id field for class: " + this.getClass().getCanonicalName());
        }
        cache.put(key, String.format("DELETE FROM %s WHERE %s = ?", tableName, idFieldName));
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
    return cache.get(key);
  }

  default String updateQuery(){
    String key = this.getClass().getCanonicalName() +"-updateQuery";
    if(!cache.containsKey(key)) {
      try {

        //get table name
        String tableName = this.getClass().getSimpleName();
        for (Table tableAnnotation : this.getClass().getDeclaredAnnotationsByType(Table.class)) {
          if (!tableAnnotation.value().equals("")) {
            tableName = tableAnnotation.value();
          }
        }

        List<String> columnNames = new ArrayList<>();
        //get id field name
        String idFieldName = null;
        for (Field field : this.getClass().getDeclaredFields()) {
          for (Annotation annotation : field.getDeclaredAnnotations()) { //get annotations on field
            if (annotation instanceof Column) {                          //if annotation is a column annotation
              Column colAnnotation = (Column) annotation;                //get the col annotation
              if (colAnnotation.id()) {
                idFieldName = colAnnotation.value().equals("") ? field.getName() : colAnnotation.value();
              } else {
                columnNames.add(colAnnotation.value().equals("") ? field.getName() : colAnnotation.value());
              }
            }
          }
        }

        if (idFieldName == null) {
          throw new FieldMapperException("Unable to find id field for class: " + this.getClass().getCanonicalName());
        }


        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (String name : columnNames) {
          if (first) {
            first = false;
          } else {
            stringBuilder.append(",");
          }
          stringBuilder.append(name + "=?");
        }

        cache.put(key, String.format("UPDATE %s set %s where %s=?", tableName, stringBuilder.toString(), idFieldName));
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
    return cache.get(key);
  }

  default String insertQuery(){
    String key = this.getClass().getCanonicalName() +"-insertQuery";
    if(!cache.containsKey(key)) {
      try {

        //get table name
        String tableName = this.getClass().getSimpleName();
        for (Table tableAnnotation : this.getClass().getDeclaredAnnotationsByType(Table.class)) {
          if (!tableAnnotation.value().equals("")) {
            tableName = tableAnnotation.value();
          }
        }

        List<String> columnNames = new ArrayList<>();
        //get id field name
        String idFieldName = null;
        for (Field field : this.getClass().getDeclaredFields()) {
          for (Annotation annotation : field.getDeclaredAnnotations()) { //get annotations on field
            if (annotation instanceof Column) {                          //if annotation is a column annotation
              Column colAnnotation = (Column) annotation;                //get the col annotation
              if (colAnnotation.id()) {
                idFieldName = colAnnotation.value().equals("") ? field.getName() : colAnnotation.value();
              } else {
                columnNames.add(colAnnotation.value().equals("") ? field.getName() : colAnnotation.value());
              }
            }
          }
        }

        if (idFieldName == null) {
          throw new FieldMapperException("Unable to find id field for class: " + this.getClass().getCanonicalName());
        }


        StringBuilder colNames = new StringBuilder();
        StringBuilder colParams = new StringBuilder();

        colNames.append("(");
        colParams.append("(");

        boolean first = true;
        for (String name : columnNames) {
          if (first) {
            first = false;
          } else {
            colNames.append(",");
            colParams.append(",");
          }
          colNames.append(name);
          colParams.append("?");
        }
        colNames.append(")");
        colParams.append(")");

        cache.put(key, String.format("INSERT INTO %s %s values %s", tableName, colNames.toString(), colParams.toString()));
      } catch (Exception ex) {
        throw new FieldMapperException(ex);
      }
    }
    return cache.get(key);
  }
}
