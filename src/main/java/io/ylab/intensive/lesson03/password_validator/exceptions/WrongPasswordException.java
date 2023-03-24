package io.ylab.intensive.lesson03.password_validator.exceptions;

public class WrongPasswordException extends Exception {
  public WrongPasswordException(String message) {
    super(message);
  }
}
