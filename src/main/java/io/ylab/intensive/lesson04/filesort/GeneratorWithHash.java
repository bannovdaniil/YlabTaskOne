package io.ylab.intensive.lesson04.filesort;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class GeneratorWithHash {

  public long generate(File fileName, int count) throws IOException {
    long hashCode = 0L;
    Random random = new Random();
    try (PrintWriter pw = new PrintWriter(fileName)) {
      for (int i = 0; i < count; i++) {
        long number = random.nextLong();
        pw.println(number);
        hashCode ^= number;
      }
      pw.flush();
    }
    return hashCode;
  }
}