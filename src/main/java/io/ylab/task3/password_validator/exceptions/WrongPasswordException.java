package io.ylab.task3.password_validator.exceptions;

public class WrongPasswordException extends Exception {
  public WrongPasswordException(String message) {
    super(message);
  }
}
