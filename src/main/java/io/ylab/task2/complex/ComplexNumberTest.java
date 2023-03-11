package io.ylab.task2.complex;

public class ComplexNumberTest {
  public static void main(String[] args) {
    ComplexNumber cn1 = new ComplexNumber(1, 1);
    ComplexNumber cn2 = new ComplexNumber(2, 2);
    ComplexNumber cn3 = new ComplexNumber(3);

    System.out.println(cn1);
    System.out.println(cn2);

    ComplexNumber cnAdd = cn1.add(cn2);
    System.out.println("Сложение: " + cnAdd);

    ComplexNumber cnSub = cn1.sub(cn2);
    System.out.println("Вычитание: " + cnSub);

    ComplexNumber cnMul = cn1.multiply(cn2);
    System.out.println("Умножение: " + cnMul);

    printModule(cn1);
    printModule(cn2);
    printModule(cn3);

  }

  private static void printModule(ComplexNumber cn3) {
    System.out.println("Модуль числа " + cn3 + " = " + cn3.mod());
  }
}