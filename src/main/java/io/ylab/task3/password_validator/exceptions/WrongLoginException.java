package io.ylab.task3.password_validator.exceptions;

public class WrongLoginException extends Exception {
  public WrongLoginException(String message) {
    super(message);
  }
}
