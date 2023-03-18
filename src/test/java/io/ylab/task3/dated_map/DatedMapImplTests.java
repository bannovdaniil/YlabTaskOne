package io.ylab.task3.dated_map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Date;
import java.util.Set;

class DatedMapImplTests {
  private DatedMap datedMap;

  @ParameterizedTest
  @DisplayName("Test Two Map")
  @CsvSource({
      "key1, value1, key2, value2",
      "key4, value4, key3, value3",
  })
  void dateMapWithTwoMap(String key1, String value1, String key2, String value2) {
    datedMap = new DatedMapImplTwoMap();

    testImpl(key1, value1, key2, value2);
  }

  @ParameterizedTest
  @DisplayName("Test Map with Entry")
  @CsvSource({
      "key1, value1, key2, value2",
      "key4, value4, key3, value3",
  })
  void dateMapWithEntry(String key1, String value1, String key2, String value2) {
    datedMap = new DatedMapImpl();

    testImpl(key1, value1, key2, value2);
  }

  private void testImpl(String key1, String value1, String key2, String value2) {
    String expect = value1;

    Date expectDate = new Date();
    datedMap.put(key1, value1);
    Assertions.assertEquals(expect, datedMap.get(key1));

    Date resultDate = datedMap.getKeyLastInsertionDate(key1);
    Assertions.assertTrue(resultDate != null && (expectDate.before(resultDate) || expectDate.equals(resultDate)));

    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    expect = value2;
    expectDate = new Date();
    datedMap.put(key1, value2);
    Assertions.assertEquals(expect, datedMap.get(key1));

    resultDate = datedMap.getKeyLastInsertionDate(key1);
    Assertions.assertTrue(resultDate != null && (expectDate.before(resultDate) || expectDate.equals(resultDate)));

    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    expect = value2;
    expectDate = new Date();
    datedMap.put(key2, value2);
    Assertions.assertEquals(expect, datedMap.get(key2));

    resultDate = datedMap.getKeyLastInsertionDate(key2);
    Assertions.assertTrue(resultDate != null && (expectDate.before(resultDate) || expectDate.equals(resultDate)));

    Assertions.assertTrue(datedMap.containsKey(key1));

    Set<String> expectSet = Set.of(key1, key2);
    Assertions.assertEquals(expectSet, datedMap.keySet());

    datedMap.remove(key1);
    Assertions.assertFalse(datedMap.containsKey(key1));
    Assertions.assertNull(datedMap.get(key1));
    Assertions.assertNull(datedMap.getKeyLastInsertionDate(key1));
  }
}