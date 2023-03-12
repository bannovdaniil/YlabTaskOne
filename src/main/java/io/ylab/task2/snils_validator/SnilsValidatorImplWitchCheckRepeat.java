package io.ylab.task2.snils_validator;

/**
 * По утверждению некоторых источников, цифры в номере не должны повторяться более 3х раз
 * В номере XXX-XXX-XXX не может присутствовать одна и та же цифра три раза подряд.
 * Дефисы при этой проверке игнорируются, т.е. неверными будут все нижеследующие примеры СНИЛСов:
 * <p>
 * https://rostov-tfoms.ru/services/zasedaniya-rabochih-grupp/erp/pravila-rascheta-kontrolnoj-summy-snils
 */

public class SnilsValidatorImplWitchCheckRepeat implements SnilsValidator {

  @Override
  public boolean validate(String snils) {
    if (!isValidCnilsFormat(snils)) {
      return false;
    }
    String onlyNumber = snils.replaceAll("\\D", "");

    if (onlyNumber.length() != 11) {
      return false;
    }

    int checkSum = getCheckSumFromNumber(onlyNumber);
    long realCheckSum = 0;
    int repeatCount = 1;
    int lastDigit = -1;

    for (int i = 0; i < 9; i++) {
      int digit = Character.digit(onlyNumber.charAt(i), 10);
      realCheckSum += (9L - i) * digit;
      if (digit == lastDigit) {
        repeatCount++;
        if (repeatCount == 3) {
          return false;
        }
      } else {
        repeatCount = 1;
      }
      lastDigit = digit;
    }

    if (realCheckSum > 100) {
      realCheckSum = realCheckSum % 101;
    }
    if (realCheckSum == 100) {
      realCheckSum = 0;
    }

    return checkSum == realCheckSum;
  }

  private boolean isValidCnilsFormat(String snils) {
    boolean result;
    result = snils.matches("^\\d{11}$");
    result |= snils.matches("^\\d{3}-\\d{3}-\\d{3}[- ]\\d{2}$");

    return result;
  }

  private static int getCheckSumFromNumber(String onlyNumber) {
    return Integer.parseInt(onlyNumber.substring(onlyNumber.length() - 2), 10);
  }
}
