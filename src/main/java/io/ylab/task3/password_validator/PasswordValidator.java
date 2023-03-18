package io.ylab.task3.password_validator;

import io.ylab.task3.password_validator.exceptions.WrongLoginException;
import io.ylab.task3.password_validator.exceptions.WrongPasswordException;

public class PasswordValidator {
  private final static int MAX_LENGTH = 20;
  private final static String LOGIN_MASK = "^[A-Za-z0-9_]*$";

  public static boolean isValid(String login, String password, String confirmPassword) {
    boolean isValid = true;
    try {
      if (!login.matches(LOGIN_MASK)) {
        throw new WrongLoginException("Логин содержит недопустимые символы");
      }
      if (login.length() >= MAX_LENGTH) {
        throw new WrongLoginException("Логин слишком длинный");
      }

      if (!password.matches(LOGIN_MASK)) {
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
}
