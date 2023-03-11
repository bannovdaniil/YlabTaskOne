package io.ylab.task2.snils_validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SnilsValidatorImplTests {

  @ParameterizedTest
  @DisplayName("SNILS validate")
  @CsvSource({
      "123, false",
      "123-123-123-123, false",
      "871-567-120 00, false",
      "231-339-702 41, false",
      "612-850-337 71, false",
      "01468870570, false",
      "90114404441, true",
      "022-297-426 36, true",
      "178-842-615 20, true",
      "439-785-289 54, true",
      "049-672-517 97, true",
      "678-425-528 45, true",
      "658-822-488 50, true",
      "991-089-289 65, true"
  })
  void validate(String snilsNumber, boolean expected) {
    SnilsValidator snilsValidator = new SnilsValidatorImpl();

    Assertions.assertEquals(expected, snilsValidator.validate(snilsNumber));
  }
}