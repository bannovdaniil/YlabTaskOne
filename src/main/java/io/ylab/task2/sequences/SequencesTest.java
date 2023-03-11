package io.ylab.task2.sequences;

public class SequencesTest {
  private final static int SEQUENCE_COUNT = 10;

  public static void main(String[] args) {
    System.out.println("With String Builder:");
    showMeTheNumber(SEQUENCE_COUNT, new SequencesImpl());

    System.out.println(System.lineSeparator() + "With Function interface:");
    showMeTheNumber(SEQUENCE_COUNT, new SequencesImplFunction());

  }

  public static void showMeTheNumber(int count, SequenceGenerator sequenceGenerator) {
    sequenceGenerator.a(count);
    sequenceGenerator.b(count);
    sequenceGenerator.c(count);
    sequenceGenerator.d(count);
    sequenceGenerator.e(count);
    sequenceGenerator.f(count);
    sequenceGenerator.g(count);
    sequenceGenerator.h(count);
    sequenceGenerator.i(count);
    sequenceGenerator.j(count);
  }
}
