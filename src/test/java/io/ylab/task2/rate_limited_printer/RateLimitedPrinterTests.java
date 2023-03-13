package io.ylab.task2.rate_limited_printer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class RateLimitedPrinterTests {
  private PrintStream systemOut;
  private ByteArrayOutputStream testOut;

  @BeforeEach
  void setUp() {
    systemOut = System.out;
    testOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testOut));
  }

  @AfterEach
  void tearDown() {
    System.setOut(systemOut);
  }

  @Test
  void print() {
    int interval = 1000;
    RateLimitedPrinter rateLimitedPrinter = new RateLimitedPrinter(interval);

    long result = System.currentTimeMillis();
    for (int i = 0; i < 1_000_000_000; i++) {
      rateLimitedPrinter.print("time");
    }
    result = System.currentTimeMillis() - result;
    result = result / interval;

    String[] countMessage = testOut.toString().split("\n");
    long expected = countMessage.length - 1;

    Assertions.assertTrue(expected >= result,
        "Расчетное количество сообщений: " + result + ", фактическое количество сообщений: " + expected);
  }
}