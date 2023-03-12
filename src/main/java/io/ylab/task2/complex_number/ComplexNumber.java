package io.ylab.task2.complex_number;

/**
 * 1. Создание нового числа по действительной части (конструктор с 1 параметром)
 * 2. Создание нового числа по действительной и мнимой части (конструктор с 2 параметрами)
 * 3. Сложение
 * 4. Вычитание
 * 5. Умножение
 * 6. Операция получения модуля
 * 7. преобразование в строку (toString)
 * (арифметические действия должны создавать новый экземпляр класса)
 */
public class ComplexNumber {
  private final double real;
  private final double imaginary;

  public ComplexNumber(double real) {
    this.real = real;
    this.imaginary = 0.0;
  }

  public ComplexNumber(double real, double imaginary) {
    this.real = real;
    this.imaginary = imaginary;
  }

  public ComplexNumber add(ComplexNumber a) {
    return new ComplexNumber(this.real + a.getReal(), this.imaginary + a.getImaginary());
  }

  public ComplexNumber sub(ComplexNumber a) {
    return new ComplexNumber(this.real - a.getReal(), this.imaginary - a.getImaginary());
  }

  public ComplexNumber multiply(ComplexNumber a) {
    return
        new ComplexNumber(
            this.real * a.getReal() - this.imaginary * a.getImaginary(),
            this.real * a.getImaginary() + this.getImaginary() * a.getReal()
        );
  }

  public double mod() {
    return Math.sqrt(real * real + imaginary * imaginary);
  }

  public double getReal() {
    return real;
  }

  public double getImaginary() {
    return imaginary;
  }

  @Override
  public String toString() {
    return "(" +
        real +
        " + " +
        imaginary +
        "i" +
        ')';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ComplexNumber that = (ComplexNumber) o;

    if (Double.compare(that.real, real) != 0) return false;
    return Double.compare(that.imaginary, imaginary) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(real);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(imaginary);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
