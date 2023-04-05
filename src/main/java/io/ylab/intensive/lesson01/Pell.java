package io.ylab.intensive.lesson01;

import java.util.Scanner;

/**
 * Pell numbers Числа Пелля.
 * На вход подается число n (0 <= n <= 30), необходимо распечатать n-e число
 */
public class Pell {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();

            System.out.println(pellNumber(n));
        }
    }

    private static long pellNumber(int n) {
        if (n < 0 || n > 30) {
            throw new IllegalArgumentException("Ошибка: число не может быть отрицательным или больше 30.");
        } else if (n <= 2) {
            return n;
        }

        long result = 0;
        long slow = 0;
        long fast = 1;
        for (int i = 0; i < n - 1; i++) {
            result = 2 * fast + slow;
            slow = fast;
            fast = result;
        }
        return result;
    }
}
