package io.ylab.task2.sequences;

public class SequencesImpl implements SequenceGenerator {
  @Override
  public void a(int n) {
    StringBuilder sb = new StringBuilder("A. ");
    int seed = 2;
    while (n-- > 0) {
      sb.append(seed);
      if (n != 0) {
        sb.append(", ");
      }
      seed += 2;
    }
    System.out.println(sb);
  }

  @Override
  public void b(int n) {

  }

  @Override
  public void c(int n) {

  }

  @Override
  public void d(int n) {

  }

  @Override
  public void e(int n) {

  }

  @Override
  public void f(int n) {

  }

  @Override
  public void g(int n) {

  }

  @Override
  public void h(int n) {

  }

  @Override
  public void i(int n) {

  }

  @Override
  public void j(int n) {

  }
}
