package io.ylab.task2.rate;

public class RateLimitedPrinter {
  private final int interval;
  private long lastTime;

  public RateLimitedPrinter(int interval) {
    this.interval = interval;
    lastTime = System.currentTimeMillis();
  }

  public void print(String message) {
    if (System.currentTimeMillis() - lastTime >= interval) {
      lastTime = System.currentTimeMillis();
      System.out.println(message);
    }
  }
}
