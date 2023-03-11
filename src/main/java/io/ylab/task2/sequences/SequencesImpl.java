package io.ylab.task2.sequences;

import java.math.BigDecimal;

public class SequencesImpl implements SequenceGenerator {

  @Override
  public void a(int n) {
    StringBuilder sb = new StringBuilder("A. ");
    long seed = 2;
    int count = 0;
    while (count++ < n) {
      sb.append(seed);
      sb.append(", ");
      seed += 2;
    }
    printSequence(sb);
  }

  @Override
  public void b(int n) {
    StringBuilder sb = new StringBuilder("B. ");
    long seed = 1;
    int count = 0;
    while (count++ < n) {
      sb.append(seed);
      sb.append(", ");
      seed += 2;
    }
    printSequence(sb);
  }

  @Override
  public void c(int n) {
    StringBuilder sb = new StringBuilder("C. ");
    long result = 1;
    long seed = 2;
    int count = 0;
    while (count++ < n) {
      sb.append(result);
      sb.append(", ");
      result = seed * seed;
      seed++;
    }
    printSequence(sb);
  }

  @Override
  public void d(int n) {
    StringBuilder sb = new StringBuilder("D. ");
    long result = 1;
    int seed = 2;
    int count = 0;
    while (count++ < n) {
      sb.append(result);
      sb.append(", ");
      result = (long) Math.pow(seed, 3);
      seed++;
    }
    printSequence(sb);
  }

  @Override
  public void e(int n) {
    StringBuilder sb = new StringBuilder("E. ");
    int count = 0;
    while (count++ < n) {
      sb.append(count % 2 == 0 ? -1 : 1);
      sb.append(", ");
    }
    printSequence(sb);
  }

  @Override
  public void f(int n) {
    StringBuilder sb = new StringBuilder("F. ");
    int count = 0;
    while (count++ < n) {
      sb.append(count % 2 == 0 ? -count : count);
      sb.append(", ");
    }
    printSequence(sb);
  }

  @Override
  public void g(int n) {
    StringBuilder sb = new StringBuilder("G. ");
    long count = 1;
    long seed = 1;
    while (count++ <= n) {
      sb.append(count % 2 == 0 ? seed : -seed);
      sb.append(", ");
      seed = count * count;
    }
    printSequence(sb);
  }

  @Override
  public void h(int n) {
    StringBuilder sb = new StringBuilder("H. ");
    int count = 0;
    long seed = 1;
    while (count++ < n) {
      sb.append(count % 2 == 0 ? 0 : seed++);
      sb.append(", ");
    }
    printSequence(sb);
  }

  @Override
  public void i(int n) {
    StringBuilder sb = new StringBuilder("I. ");
    int count = 1;
    BigDecimal seed = new BigDecimal(1);
    while (count++ <= n) {
      sb.append(seed);
      sb.append(", ");
      seed = seed.multiply(new BigDecimal(count));
    }
    printSequence(sb);
  }

  @Override
  public void j(int n) {
    StringBuilder sb = new StringBuilder("J. ");
    int count = 1;
    long slow = 0;
    long fast = 1;
    long result = 1;
    while (count++ <= n) {
      sb.append(result);
      sb.append(", ");
      result = slow + fast;
      slow = fast;
      fast = result;
    }
    printSequence(sb);
  }

  private static void printSequence(StringBuilder sb) {
    if (sb.length() > 3) {
      sb.delete(sb.length() - 2, sb.length() - 1);
    }
    System.out.println(sb);
  }


}
