package io.ylab.task3.dated_map;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatedMapImpl extends HashMap implements DatedMap {
  private final Map<String, Date> storeKeyDate = new HashMap<>();

  @Override
  public void put(String key, String value) {
    super.put(key, value);
    storeKeyDate.put(key, new Date());
  }

  @Override
  public String get(String key) {
    return (String) super.get(key);
  }

  @Override
  public boolean containsKey(String key) {
    return super.containsKey(key);
  }

  @Override
  public void remove(String key) {
    super.remove(key);
    storeKeyDate.remove(key);
  }

  @Override
  public Date getKeyLastInsertionDate(String key) {
    return storeKeyDate.get(key);
  }
}
