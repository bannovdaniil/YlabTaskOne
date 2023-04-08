package io.ylab.intensive.lesson03.password_validator;

import io.ylab.intensive.lesson03.password_validator.exceptions.WrongLoginException;
import io.ylab.intensive.lesson03.password_validator.exceptions.WrongPasswordException;

public class PasswordValidator {
    private final static int MAX_LENGTH = 20;
    private final static String LOGIN_MASK = "^[A-Za-z0-9_]*$";
    private final static String LOGIN_MASK_AZ = ".*[A-Za-z]+.*";
    private final static String LOGIN_MASK_NUM = ".*[0-9]+.*";
    private final static String LOGIN_MASK_UNDER = "_";

    public static boolean isValid(String login, String password, String confirmPassword) {
        boolean isValid = true;
        try {

            if (isCorrectMaskCharacter(login)) {
                throw new WrongLoginException("Логин содержит недопустимые символы");
            }
            if (login.length() >= MAX_LENGTH) {
                throw new WrongLoginException("Логин слишком длинный");
            }

            if (isCorrectMaskCharacter(password)) {
                throw new WrongPasswordException("Пароль содержит недопустимые символы");
            }
            if (password.length() >= MAX_LENGTH) {
                throw new WrongLoginException("Пароль слишком длинный");
            }

            if (!password.equals(confirmPassword)) {
                throw new WrongPasswordException("Пароль и подтверждение не совпадают");
            }
        } catch (WrongPasswordException | WrongLoginException err) {
            System.out.println(err.getMessage());
            isValid = false;
        }

        return isValid;
    }

    private static boolean isCorrectMaskCharacter(String word) {
        return !(
                word != null
                        && word.length() > 0
                        && word.matches(LOGIN_MASK)
        );
    }

    private static boolean isCorrectMaskCharacterMaskCharacterNeedAll(String word) {
        return (word.matches(LOGIN_MASK)
                & word.matches(LOGIN_MASK_AZ)
                & word.matches(LOGIN_MASK_NUM)
                & word.contains(LOGIN_MASK_UNDER)
        );
    }
}
