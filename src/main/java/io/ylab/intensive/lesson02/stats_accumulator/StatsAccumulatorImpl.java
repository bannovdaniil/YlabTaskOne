package io.ylab.intensive.lesson02.stats_accumulator;

public class StatsAccumulatorImpl implements StatsAccumulator {
    private double sum;
    private int min;
    private int max;
    private int count;

    public StatsAccumulatorImpl() {
        this.sum = 0.0;
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
        this.count = 0;
    }

    @Override
    public void add(int value) {
        sum += value;
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }
        count++;
    }

    @Override
    public int getMin() {
        if (count == 0) {
            throw new IllegalStateException("Not have any elements.");
        }
        return min;
    }

    @Override
    public int getMax() {
        if (count == 0) {
            throw new IllegalStateException("Not have any elements.");
        }
        return max;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        if (count == 0) {
            throw new IllegalStateException("Not have any elements.");
        }
        return sum / count;
    }
}
