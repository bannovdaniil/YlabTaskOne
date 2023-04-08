package io.ylab.intensive.lesson03.dated_map;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatedMapImpl implements DatedMap {
    private final Map<String, Map.Entry<String, Date>> store = new HashMap<>();

    @Override
    public void put(String key, String value) {
        store.put(key, Map.entry(value, new Date()));
    }

    @Override
    public String get(String key) {
        Map.Entry<String, Date> entry = store.get(key);
        return entry == null ? null : entry.getKey();
    }

    @Override
    public boolean containsKey(String key) {
        return store.containsKey(key);
    }

    @Override
    public void remove(String key) {
        store.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return store.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        Map.Entry<String, Date> entry = store.get(key);
        return entry == null ? null : entry.getValue();
    }
}