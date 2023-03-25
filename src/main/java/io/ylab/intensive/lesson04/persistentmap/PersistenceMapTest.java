package io.ylab.intensive.lesson04.persistentmap;

import io.ylab.intensive.lesson04.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap = new PersistentMapImpl(dataSource);

    // Написать код демонстрации работы
    testInit(persistentMap, "first");

    testPutAndGet(persistentMap, "key1", "value1");
    testPutAndGet(persistentMap, "key1", "newValue1");

    testContainsKey(persistentMap, "key2", false);
    testPutAndGet(persistentMap, "key2", "value2");
    testContainsKey(persistentMap, "key2", true);

    testPutAndGet(persistentMap, "key3", "value3");
    testGetKeys(persistentMap, "[key1, key2, key3]");

    testRemove(persistentMap, "key2");
    testGetKeys(persistentMap, "[key1, key3]");

    System.out.println("\nclear()");
    persistentMap.clear();
    testGetKeys(persistentMap, "[]");

    testPutAndGet(persistentMap, "key4", "value4");
    testGetKeys(persistentMap, "[key4]");

    testInit(persistentMap, "second");
    testGetKeys(persistentMap, "[]");

    testPutAndGet(persistentMap, "key5", "value5");
    testPutAndGet(persistentMap, "key6", "value6");
    testGetKeys(persistentMap, "[key5, key6]");

    testInit(persistentMap, "first");
    testGetKeys(persistentMap, "[key4]");

    System.out.print("get(null): ");
    System.out.println("Assert: null == " + persistentMap.get(null));

    testPutAndGet(persistentMap, null, "testNull");
    testPutAndGet(persistentMap, "keyOk", null);
    testGetKeys(persistentMap, "[key4, null, keyOk]");

    testRemove(persistentMap, null);
    testGetKeys(persistentMap, "[key4, keyOk]");
  }

  private static void testRemove(PersistentMap persistentMap, String key) throws SQLException {
    System.out.print("\nremove(" + key + "): ");
    String expect = persistentMap.get(key);
    persistentMap.remove(key);

    testContainsKey(persistentMap, key, false);

    System.out.print("get(" + key + "): ");
    System.out.println("> null == " + persistentMap.get(key));
    if (expect != null && expect.equals(persistentMap.get(key))) {
      throw new AssertionError("Remove don't work");
    }
  }

  private static void testGetKeys(PersistentMap persistentMap, String expect) throws SQLException {
    System.out.print("\ngetKeys: ");
    System.out.println("> " + expect + " == " + persistentMap.getKeys());
    if (!expect.equals(persistentMap.getKeys().toString())) {
      throw new AssertionError("GetKeys don't work");
    }
  }

  private static void testContainsKey(PersistentMap persistentMap, String key, boolean expect) throws SQLException {
    System.out.print("\ncontainsKey(" + key + ")? ");
    System.out.println("> " + expect + " == " + persistentMap.containsKey(key));
    if (expect != persistentMap.containsKey(key)) {
      throw new AssertionError("ContainsKey don't work");
    }
  }

  private static void testPutAndGet(PersistentMap persistentMap, String key, String value) throws SQLException {
    System.out.println("\nPut: (" + key + ", " + value + ")");
    persistentMap.put(key, value);
    System.out.print("get(" + key + "): ");
    System.out.println("> " + value + " == " + persistentMap.get(key));
    if (value != null && !value.equals(persistentMap.get(key))) {
      throw new AssertionError("Put or Get don't work");
    } else if (value == null && persistentMap.get(key) != null) {
      throw new AssertionError("Put or Get don't work with null.");
    }
  }

  private static void testInit(PersistentMap persistentMap, String name) {
    System.out.println("\n\nInit: " + name);
    persistentMap.init(name);
  }

  public static DataSource initDb() throws SQLException {
    String createMapTable = ""
        + "drop table if exists persistent_map; "
        + "CREATE TABLE if not exists persistent_map (\n"
        + "   map_name varchar,\n"
        + "   KEY varchar,\n"
        + "   value varchar\n"
        + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
