package io.ylab.intensive.lesson03.password_validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class PasswordValidatorTests {
  private PrintStream systemOut;
  private ByteArrayOutputStream testOut;

  @BeforeEach
  void setUp() {
    systemOut = System.out;
    testOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testOut));
  }

  @AfterEach
  void tearDown() {
    System.setOut(systemOut);
  }

  @ParameterizedTest
  @DisplayName("Password Validator")
  @CsvSource({
      "goodLogin1234567890 , password , password, true , none",
      "abc_ABC_1234567890_ , password, password, true , none",
      "@badLogin , password, password, false, Логин содержит недопустимые символы",
      "I_am_a_long_login_very_long_Login , password, password, false, Логин слишком длинный",
      "01234567890123456789 , password, password, false, Логин слишком длинный",

      "login, paSSword_12345, paSSword_12345 , true, none",
      "login, Good_paSSword_12345, Good_paSSword_12345, true, none",
      "login, paSSword-12345, paSSword-12345 , false, Пароль содержит недопустимые символы",
      "login, I_am_a_long_password_very_long_Password, I_am_a_long_password_very_long_Password, false, Пароль слишком длинный",

      "login, password, password, true, none",
      "login, notEquals1, password2, false, Пароль и подтверждение не совпадают"
  })
  void isValid(String login, String password, String confirmPassword, boolean expect, String expectMessage) {
    boolean result = PasswordValidator.isValid(login, password, confirmPassword);
    Assertions.assertEquals(expect, result);

    if (!"none".equals(expectMessage)) {
      String resultMessage = testOut.toString().trim();
      Assertions.assertEquals(expectMessage, resultMessage);
    }
  }
}