package com.rizvn.fieldmapper.query;

import com.rizvn.fieldmapper.mapper.FieldMapper;

import java.util.List;

/**
 * @author Riz
 */
public interface DeleteQuery {
  default Integer delete(){
    List<FieldMapper.Tuple> fieldsAnnotatedByColumn = FieldMapper.findFieldsAnnotatedByColumn(this);
    for(FieldMapper.Tuple tuple : fieldsAnnotatedByColumn){
      if(tuple.column.id()){

      }
    }
    return null;
  }
}
