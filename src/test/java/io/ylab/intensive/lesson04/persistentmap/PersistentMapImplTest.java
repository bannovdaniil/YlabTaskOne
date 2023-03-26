package io.ylab.intensive.lesson04.persistentmap;

import io.ylab.intensive.lesson04.DbUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

class PersistentMapImplTest {
  private PersistentMap persistentMap;

  @BeforeEach
  void setUp() throws SQLException {
    String createMapTable = ""
        + "drop table if exists persistent_map; "
        + "CREATE TABLE if not exists persistent_map (\n"
        + "   map_name varchar,\n"
        + "   KEY varchar,\n"
        + "   value varchar\n"
        + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);

    persistentMap = new PersistentMapImpl(dataSource);
  }

  @Test
  @DisplayName("Init + clear only One")
  void init() throws SQLException {
    persistentMap.init("map1");
    persistentMap.put("map1key1", "value1");
    persistentMap.put("map1key2", "value2");

    Assertions.assertTrue(persistentMap.containsKey("map1key1"));
    Assertions.assertTrue(persistentMap.containsKey("map1key2"));

    persistentMap.init("map2");

    Assertions.assertFalse(persistentMap.containsKey("map1key1"));
    Assertions.assertFalse(persistentMap.containsKey("map1key2"));

    persistentMap.put("map2key1", "value1");
    persistentMap.put("map2key2", "value2");

    persistentMap.init("map1");

    Assertions.assertTrue(persistentMap.containsKey("map1key1"));
    Assertions.assertTrue(persistentMap.containsKey("map1key2"));

    Assertions.assertFalse(persistentMap.containsKey("map2key1"));
    Assertions.assertFalse(persistentMap.containsKey("map2key2"));

    persistentMap.clear();

    persistentMap.init("map2");

    Assertions.assertTrue(persistentMap.containsKey("map2key1"));
    Assertions.assertTrue(persistentMap.containsKey("map2key2"));

    persistentMap.init("map1");

    Assertions.assertFalse(persistentMap.containsKey("map1key1"));
    Assertions.assertFalse(persistentMap.containsKey("map1key2"));
    Assertions.assertFalse(persistentMap.containsKey("map2key1"));
    Assertions.assertFalse(persistentMap.containsKey("map2key2"));
  }

  @Test
  void containsKey() throws SQLException {
    Assertions.assertFalse(persistentMap.containsKey("key1"));

    persistentMap.put("key1", "value1");
    Assertions.assertTrue(persistentMap.containsKey("key1"));
  }

  @Test
  @DisplayName("Put and Get")
  void putAndgetKeys() throws SQLException {
    String expect1 = "value1";
    String expect2 = "value2";

    Assertions.assertNotEquals(expect1, persistentMap.get("key1"));
    Assertions.assertNotEquals(expect1, persistentMap.get("key2"));

    persistentMap.put("key1", expect1);
    persistentMap.put("key2", expect2);

    Assertions.assertEquals(expect1, persistentMap.get("key1"));
    Assertions.assertEquals(expect2, persistentMap.get("key2"));
  }

  @Test
  void remove() throws SQLException {
    persistentMap.put("key1", "value1");

    Assertions.assertTrue(persistentMap.containsKey("key1"));

    persistentMap.remove("key1");

    Assertions.assertFalse(persistentMap.containsKey("key1"));
  }

  @Test
  void clear() throws SQLException {
    persistentMap.put("key1", "value1");
    persistentMap.put("key2", "value2");
    persistentMap.put("key3", "value3");

    Assertions.assertTrue(persistentMap.containsKey("key1"));
    Assertions.assertTrue(persistentMap.containsKey("key2"));
    Assertions.assertTrue(persistentMap.containsKey("key3"));

    persistentMap.clear();

    Assertions.assertFalse(persistentMap.containsKey("key1"));
    Assertions.assertFalse(persistentMap.containsKey("key2"));
    Assertions.assertFalse(persistentMap.containsKey("key3"));
  }
}