package io.ylab.task2.snils_validator;

public class SnilsValidatorTest {
  public static void main(String[] args) {
    System.out.println(new SnilsValidatorImpl().validate("01468870570")); //false
    System.out.println(new SnilsValidatorImpl().validate("90114404441")); //true
    System.out.println(new SnilsValidatorImpl().validate("771-713-240 98")); //true
    System.out.println(new SnilsValidatorImpl().validate("330-445-751 47")); //false
  }
}
