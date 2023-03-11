package io.ylab.task2.complex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


public class ComplexNumberTests {
  private ComplexNumber cn1;
  private ComplexNumber cn2;

  @BeforeEach
  void setUp() {
    cn1 = new ComplexNumber(1, 1);
    cn2 = new ComplexNumber(2, 2);
  }

  @Test
  @DisplayName("Additional Complex Number")
  void add() {
    ComplexNumber expected = new ComplexNumber(3, 3);
    ComplexNumber result = cn1.add(cn2);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Subtracting Complex Number")
  void sub() {
    ComplexNumber expected = new ComplexNumber(-1, -1);
    ComplexNumber result = cn1.sub(cn2);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Multiply Complex Number")
  void multiply() {
    ComplexNumber expected = new ComplexNumber(0, 4);
    ComplexNumber result = cn1.multiply(cn2);

    Assertions.assertEquals(expected, result);
  }

  @DisplayName("Module of Complex Number")
  @ParameterizedTest
  @CsvSource({
      "1.41421356, 1.0, 1.0",
      "2.82842712, 2.0, 2.0",
      "3.0, 3.0, 0.0"
  })
  void mod(double expected, double real, double imaginary) {
    double result = new ComplexNumber(real, imaginary).mod();

    Assertions.assertEquals(expected, result, 0.0001);
  }
}