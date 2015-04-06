package com.rizvn.fieldmapper.map;

import com.rizvn.fieldmapper.typehandler.TypeHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Riz
 */
public class ResultMap extends HashMap<String, Object> implements Map<String, Object> {
  public <T> T getTyped(String key){
    Object value = get(key);
    return (T) value;
  }

  public <T> T getTyped(Enum keyEnum){
    return getTyped(keyEnum.name());
  }

  public <T> T getTyped(Enum keyEnum, TypeHandler typeHandler){
    return getTyped(keyEnum.name(), typeHandler);
  }

  public <T> T getTyped(String key, TypeHandler typeHandler){
    Object value = get(key);
    return (T) typeHandler.transform(value);
  }

  public static List<ResultMap> create(List<Map<String, Object>> results){
    List<ResultMap> resultMaps = new ArrayList<>();
    for(Map<String, Object> map : results){
      ResultMap resultMap = new ResultMap();
      for (Map.Entry<String, Object> entry : map.entrySet()){
        resultMap.put(entry.getKey(),entry.getValue());
      }
      resultMaps.add(resultMap);
    }
    return resultMaps;
  }

}
