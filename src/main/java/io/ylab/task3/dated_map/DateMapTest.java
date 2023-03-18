package io.ylab.task3.dated_map;

public class DateMapTest {

  public static void main(String[] args) throws InterruptedException {
    DatedMap datedMap = new DatedMapImpl();
    testMethods(datedMap);
    DatedMap datedMap2 = new DatedMapImplTwoMap();
    testMethods(datedMap2);
    DatedMap datedMap3 = new DatedMapImplWithEntry();
    testMethods(datedMap3);
  }

  private static void testMethods(DatedMap datedMap) throws InterruptedException {
    System.out.println(System.lineSeparator() + datedMap.getClass());
    System.out.println("-".repeat(30));
    datedMap.put("key1", "value1");

    System.out.print("Date key1: ");
    System.out.println(datedMap.getKeyLastInsertionDate("key1"));
    System.out.print("Value key1: ");
    System.out.println(datedMap.get("key1"));

    System.out.println("Wait 1sec.");
    Thread.sleep(1000L);
    datedMap.put("key2", "value2");

    System.out.print("Date key2: ");
    System.out.println(datedMap.getKeyLastInsertionDate("key2"));
    System.out.print("Value key2: ");
    System.out.println(datedMap.get("key2"));

    System.out.print("KeySet: ");
    System.out.println(datedMap.keySet());

    System.out.print("Contains key2? : ");
    System.out.println(datedMap.containsKey("key2"));

    System.out.println("Remove key2.");
    datedMap.remove("key2");

    System.out.print("Contains key2? : ");
    System.out.println(datedMap.containsKey("key2"));

    System.out.print("Value key2: ");
    System.out.println(datedMap.get("key2"));

    System.out.print("Date key2: ");
    System.out.println(datedMap.getKeyLastInsertionDate("key3"));
  }
}
