package io.ylab.task2.snils_validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SnilsValidatorImplTests {

  @ParameterizedTest
  @DisplayName("SNILS validate")
  @CsvSource({
      "871-567-120 00, false",
      "231-339-702 41, false",
      "612-850-337 71, false",
      "666-850-337-37, true",
      "661-888-337 49, true",
      "611-818-333 71, true",
      "022-297-426 36, true",
      "666z850-337-37, false",
      "666 850-337-37, false",
      "a66-850-337-37, false",
      "123, false",
      "123-123-123-123, false",
      "01468870570, false",
      "90114404441, true",
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

  /**
   * По утверждению некоторых источников, цифры в номере не должны повторяться более 3х раз
   * В номере XXX-XXX-XXX не может присутствовать одна и та же цифра три раза подряд.
   * Дефисы при этой проверке игнорируются, т.е. неверными будут все нижеследующие примеры СНИЛСов:
   * <p>
   * https://rostov-tfoms.ru/services/zasedaniya-rabochih-grupp/erp/pravila-rascheta-kontrolnoj-summy-snils
   */
  @ParameterizedTest
  @DisplayName("SNILS validate Witch Repeat Number")
  @CsvSource({
      "666-850-337 37, false",
      "661-888-337 49, false",
      "611-818-333 71, false",
      "022-297-426 36, false",
      "123, false",
      "123-123-123-123, false",
      "871-567-120 00, false",
      "231-339-702 41, false",
      "612-850-337 71, false",
      "01468870570, false",
      "90114404441, true",
      "178-842-615 20, true",
      "439-785-289 54, true",
      "049-672-517 97, true",
      "678-425-528 45, true",
      "658-822-488 50, true",
      "991-089-289 65, true"
  })
  void validateWitchRepeatNumber(String snilsNumber, boolean expected) {
    SnilsValidator snilsValidator = new SnilsValidatorImplWitchCheckRepeat();

    Assertions.assertEquals(expected, snilsValidator.validate(snilsNumber));
  }
}