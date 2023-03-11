package io.ylab.task2.stats;

public class StatsAccumulatorTest {
  public static void main(String[] args) {
    StatsAccumulator statsAccumulator = new StatsAccumulatorImpl();

    statsAccumulator.add(0);
    showState(statsAccumulator);

    statsAccumulator.add(10);
    showState(statsAccumulator);

    statsAccumulator.add(40);
    showState(statsAccumulator);

    statsAccumulator.add(-10);
    showState(statsAccumulator);

  }

  private static void showState(StatsAccumulator statsAccumulator) {
    System.out.println(System.lineSeparator() + "State:");
    System.out.println("Count: " + statsAccumulator.getCount());
    System.out.println("Avg: " + statsAccumulator.getAvg());
    System.out.println("Min: " + statsAccumulator.getMin());
    System.out.println("Max: " + statsAccumulator.getMax());
  }
}
