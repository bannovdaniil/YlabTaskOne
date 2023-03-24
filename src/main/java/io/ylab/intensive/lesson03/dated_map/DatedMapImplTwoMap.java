package io.ylab.intensive.lesson03.dated_map;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatedMapImplTwoMap implements DatedMap {
  private final Map<String, String> store = new HashMap<>();
  private final Map<String, Date> storeKeyDate = new HashMap<>();

  @Override
  public void put(String key, String value) {
    store.put(key, value);
    storeKeyDate.put(key, new Date());
  }

  @Override
  public String get(String key) {
    return store.get(key);
  }

  @Override
  public boolean containsKey(String key) {
    return store.containsKey(key);
  }

  @Override
  public void remove(String key) {
    store.remove(key);
    storeKeyDate.remove(key);
  }

  @Override
  public Set<String> keySet() {
    return storeKeyDate.keySet();
  }

  @Override
  public Date getKeyLastInsertionDate(String key) {
    return storeKeyDate.get(key);
  }
}
