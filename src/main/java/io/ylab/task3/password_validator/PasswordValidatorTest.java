package io.ylab.task3.password_validator;

public class PasswordValidatorTest {
  public static void main(String[] args) {
    testLogin("");
    testLogin("123");
    testLogin("num");
    testLogin("num_num");
    testLogin("num_NUM_123");
    testLogin("goodLogin1234567890");
    testLogin("abc_ABC_1234567890_");
    testLogin("@badLogin");
    testLogin("I_am_a_long_login_very_long_Login");

    testPassword("paSSword_12345");
    testPassword("Good_paSSword_12345");
    testPassword("paSSword-12345");
    testPassword("I_am_a_long_password_very_long_Password");

    testConfirmPassword("123", "123");
    testConfirmPassword("1234", "12345");
  }

  private static void testConfirmPassword(String password, String confirmPassword) {
    System.out.print("Test password: \"" + password + "\" & \"" + confirmPassword + " - ");
    if (PasswordValidator.isValid("login", password, confirmPassword)) {
      System.out.println("good");
    }
  }

  private static void testPassword(String password) {
    System.out.print("Test password: \"" + password + "\" - ");
    if (PasswordValidator.isValid("login", password, password)) {
      System.out.println("good");
    }
  }

  private static void testLogin(String login) {
    System.out.print("Test login: \"" + login + "\" - ");
    if (PasswordValidator.isValid(login, "123", "123"))
      System.out.println("good");
  }

}
