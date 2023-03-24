package io.ylab.intensive.lesson02.sequence_generator;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;

public class SequencesImplFunction implements SequenceGenerator {

  @Override
  public void a(int n) {
    printSequences("A. ", n, 2, (x) -> x + 2);
  }

  @Override
  public void b(int n) {
    printSequences("B. ", n, 1, (x) -> x + 2);
  }

  @Override
  public void c(int n) {
    printSequencesWithCount("C. ", n, 1, 2, (x) -> x * x);
  }

  @Override
  public void d(int n) {
    printSequencesWithCount("D. ", n, 1, 2, (x) -> (long) Math.pow(x, 3));
  }

  @Override
  public void e(int n) {
    printSequencesWithCount("E. ", n, 1, 0, (x) -> (x % 2 == 0) ? -1L : 1L);
  }

  @Override
  public void f(int n) {
    printSequencesWithCount("E. ", n, 1, 2, (x) -> (x % 2 == 0) ? -x : x);
  }

  @Override
  public void g(int n) {
    printSequencesWithCount("G. ", n, 1, 2, (x) -> (x % 2 == 0) ? -x * x : x * x);
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
    deleteLastComma(sb);
    System.out.println(sb);
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
    deleteLastComma(sb);
    System.out.println(sb);
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
    deleteLastComma(sb);
    System.out.println(sb);
  }

  private void printSequences(String nameSeq, int n, long beginNumber, UnaryOperator<Long> fn) {
    StringBuilder sb = new StringBuilder(nameSeq);
    long seed = beginNumber;
    int count = 0;
    while (count++ < n) {
      sb.append(seed);
      sb.append(", ");
      seed = fn.apply(seed);
    }
    deleteLastComma(sb);
    System.out.println(sb);
  }

  private void printSequencesWithCount(String nameSeq, int n, long beginNumber, long beginCount, UnaryOperator<Long> fn) {
    StringBuilder sb = new StringBuilder(nameSeq);
    long result = beginNumber;
    long seed = beginCount;
    int count = 0;
    while (count++ < n) {
      sb.append(result);
      sb.append(", ");
      result = fn.apply(seed);
      seed++;
    }
    deleteLastComma(sb);
    System.out.println(sb);
  }

  private static void deleteLastComma(StringBuilder sb) {
    if (sb.length() > 3) {
      sb.delete(sb.length() - 2, sb.length() - 1);
    }
  }
}
