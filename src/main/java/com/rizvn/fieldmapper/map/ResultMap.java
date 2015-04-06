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

  /**
   * Get Value for key, and cast to required return type
   * @param key key name
   * @param <T> return type
   * @return the value casted to the return type
   */
  public <T> T getTyped(String key){
    Object value = get(key);
    return (T) value;
  }

  /**
   * Get Value for key, and cast to required return type
   * @param keyEnum Key enum, keyEnum.name() will be used as column name
   * @param <T> return type
   * @return the value casted to the return type
   */
  public <T> T getTyped(Enum keyEnum){
    return getTyped(keyEnum.name());
  }

  /**
   * Get Value for key, and cast to required return typem using the
   * typeHandler specified
   * @param keyEnum Key enum, keyEnum.name() will be used as column name
   * @param typeHandler handler to use to convert value to return type
   * @param <T> return type
   * @return the value casted to the return type
   */
  public <T> T getTyped(Enum keyEnum, TypeHandler typeHandler){
    return getTyped(keyEnum.name(), typeHandler);
  }

  /**
   * Get Value for key, and cast to required return typem using the
   * typeHandler specified
   * @param key Key for column name
   * @param typeHandler handler to use to convert value to return type
   * @param <T> return type
   * @return the value casted to the return type
   */
  public <T> T getTyped(String key, TypeHandler typeHandler){
    Object value = get(key);
    return value == null ? null : (T) typeHandler.transform(value);
  }

  /**
   * Create a list of ResultMap from a list of maps
   * @param results List of Maps
   * @return list of result maps
   */
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
