package io.ylab.intensive.lesson04.persistentmap;

import io.ylab.intensive.lesson04.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap = new PersistentMapImpl(dataSource);

    // Написать код демонстрации работы
    System.out.println("Init First");
    persistentMap.init("first");

    System.out.println("\nPut: (key1, value1)");
    persistentMap.put("key1", "value1");
    System.out.print("get(key1): ");
    System.out.println("Assert: value1 == " + persistentMap.get("key1"));

    System.out.print("containsKey(key2)? ");
    System.out.println("Assert: false == " + persistentMap.containsKey("key2"));
    System.out.println("\nPut: (key2, value2)");
    persistentMap.put("key2", "value2");

    System.out.print("containsKey(key2)? ");
    System.out.println("Assert: true == " + persistentMap.containsKey("key2"));

    System.out.print("get(key2): ");
    System.out.println("Assert: value2 == " + persistentMap.get("key2"));

    System.out.println("\nPut: (key3, value3)");
    persistentMap.put("key3", "value3");
    System.out.print("getKeys: ");
    System.out.println("Assert: [key1, key2, key3] == " + persistentMap.getKeys());


    System.out.println("\nRemove(key2)");
    persistentMap.remove("key2");
    System.out.print("containsKey(key2)? ");
    System.out.println("Assert: false == " + persistentMap.containsKey("key2"));

    System.out.println("\nclear()");
    persistentMap.clear();
    System.out.print("getKeys: ");
    System.out.println("Assert: [] == " + persistentMap.getKeys());

    System.out.println("\nPut: (key4, value4)");
    persistentMap.put("key4", "value4");
    System.out.print("getKeys: ");
    System.out.println("Assert: [key4] == " + persistentMap.getKeys());

    System.out.println("\n\nInit Second");
    persistentMap.init("Second");
    System.out.print("getKeys: ");
    System.out.println("Assert: [] == " + persistentMap.getKeys());

    System.out.println("\nPut: (key5, value5)");
    persistentMap.put("key5", "value5");
    System.out.print("get(key5): ");
    System.out.println("Assert: value5 == " + persistentMap.get("key5"));
    System.out.println("\nPut: (key6, value6)");
    persistentMap.put("key6", "value6");
    System.out.print("get(key6): ");
    System.out.println("Assert: value6 == " + persistentMap.get("key6"));

    System.out.print("getKeys: ");
    System.out.println("Assert: [key5, key6] == " + persistentMap.getKeys());

    System.out.println("\n\nInit First");
    persistentMap.init("first");

    System.out.print("getKeys: ");
    System.out.println("Assert: [key4] == " + persistentMap.getKeys());

    System.out.print("get(null): ");
    System.out.println("Assert: null == " + persistentMap.get(null));

    System.out.println("\nPut(null, testNull): ");
    persistentMap.put(null, "testNull");
    System.out.print("get(null): ");
    System.out.println("Assert: testNull == " + persistentMap.get(null));

    System.out.println("\nPut(keyOk, null): ");
    persistentMap.put("keyOk", null);
    System.out.print("get(keyOk): ");
    System.out.println("Assert: null == " + persistentMap.get("keyOk"));

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
