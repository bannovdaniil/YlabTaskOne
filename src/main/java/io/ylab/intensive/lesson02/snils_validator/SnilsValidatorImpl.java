package io.ylab.intensive.lesson02.snils_validator;

/**
 * СНИЛС (страховой номер индивидуального лицевого счета) состоит из 11 цифр:
 * <p>
 * 1-9-я цифры — любые цифры;
 * 10-11-я цифры — контрольное число.
 * <p>
 * Маски ввода
 * XXXXXXXXXXX — маска ввода без разделителей.
 * XXX-XXX-XXX-XX — маска ввода с разделителями.
 * XXX-XXX-XXX XX — маска ввода с разделителями и с отделением контрольного числа.
 * <p>
 * Алгоритм проверки контрольного числа.
 * Вычислить сумму произведений цифр СНИЛС (с 1-й по 9-ю) на следующие коэффициенты — 9, 8, 7, 6, 5, 4, 3, 2, 1 (т.е. номера цифр в обратном порядке).
 * Вычислить контрольное число от полученной суммы следующим образом:
 * если она меньше 100, то контрольное число равно этой сумме;
 * если равна 100, то контрольное число равно 0;
 * если больше 100, то вычислить остаток от деления на 101 и далее:
 * если остаток от деления равен 100, то контольное число равно 0;
 * в противном случае контрольное число равно вычисленному остатку от деления.
 * Сравнить полученное контрольное число с двумя младшими разрядами СНИЛС. Если они равны, то СНИЛС верный.
 * <p>
 * XXXXXXXXXXX — маска ввода без разделителей.
 * XXX-XXX-XXX-XX — маска ввода с разделителями.
 * XXX-XXX-XXX XX — маска ввода с разделителями и с отделением контрольного числа.
 */
public class SnilsValidatorImpl implements SnilsValidator {

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

        for (int i = 0; i < 9; i++) {
            int digit = Character.digit(onlyNumber.charAt(i), 10);
            realCheckSum += (9L - i) * digit;
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
