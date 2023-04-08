package io.ylab.intensive.lesson02.rate_limited_printer;

public class RateLimitedPrinter {
    private final int interval;
    private long lastTime;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
        lastTime = 0;
    }

    public void print(String message) {
        if (System.currentTimeMillis() - lastTime >= interval) {
            lastTime = System.currentTimeMillis();
            System.out.println(message);
        }
    }
}
