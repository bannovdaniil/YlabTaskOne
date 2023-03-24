package io.ylab.intensive.lesson03.file_sort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Считает хешкод и проверяет количество.
 */
public class ValidatorWithSizeAndHash {
  private final File file;
  private long size;
  private long hashCode;

  public ValidatorWithSizeAndHash(File file, int size, long hashCode) {
    this.file = file;
    this.size = size;
    this.hashCode = hashCode;
  }

  public boolean isSorted() {
    try (Scanner scanner = new Scanner(new FileInputStream(file))) {
      long prev = Long.MIN_VALUE;
      while (scanner.hasNextLong()) {
        long current = scanner.nextLong();
        hashCode ^= current;
        size--;
        if (current < prev) {
          return false;
        } else {
          prev = current;
        }
      }
      return hashCode == 0 && size == 0;
    } catch (IOException ex) {
      ex.printStackTrace();
      return false;
    }
  }
}