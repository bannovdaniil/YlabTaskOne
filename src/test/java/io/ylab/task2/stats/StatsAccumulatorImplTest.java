package io.ylab.task2.stats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StatsAccumulatorImplTests {
  private static StatsAccumulator statsAccumulatorAdd;

  @BeforeAll
  static void beforeAll() {
    statsAccumulatorAdd = new StatsAccumulatorImpl();
  }

  @ParameterizedTest
  @DisplayName("StatsAccumulator test")
  @CsvSource({"0, 1, 0, 0, 0", "1, 2, 0, 1, 0.5", "4, 3, 0, 4, 1.6666", "-1, 4, -1, 4, 1"})
  void add(int value, int expectedCount, int expectedMin, int expectedMax, double expectedAvg) {
    statsAccumulatorAdd.add(value);
    Assertions.assertEquals(expectedCount, statsAccumulatorAdd.getCount());
    Assertions.assertEquals(expectedMin, statsAccumulatorAdd.getMin());
    Assertions.assertEquals(expectedMax, statsAccumulatorAdd.getMax());
    Assertions.assertEquals(expectedAvg, statsAccumulatorAdd.getAvg(), 0.0001);
  }

  @Test
  @DisplayName("Get Min from empty value")
  void exceptionThenGetMin() {
    String expectedMessage = "Not have any elements.";
    StatsAccumulator sa = new StatsAccumulatorImpl();

    IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class, sa::getMin);

    Assertions.assertEquals(expectedMessage, ex.getMessage());
  }

  @Test
  @DisplayName("Get Max from empty value")
  void exceptionThenGetMax() {
    String expectedMessage = "Not have any elements.";
    StatsAccumulator sa = new StatsAccumulatorImpl();

    IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class, sa::getMax);

    Assertions.assertEquals(expectedMessage, ex.getMessage());
  }

  @Test
  @DisplayName("Get Avg from empty value")
  void exceptionThenGetAvg() {
    String expectedMessage = "Not have any elements.";
    StatsAccumulator sa = new StatsAccumulatorImpl();

    IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class, sa::getAvg);

    Assertions.assertEquals(expectedMessage, ex.getMessage());
  }

}