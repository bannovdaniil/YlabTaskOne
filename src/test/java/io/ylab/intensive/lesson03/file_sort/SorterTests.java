package io.ylab.intensive.lesson03.file_sort;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;

class SorterTests {
  @AfterAll
  static void afterAll() {
    File tempFile = new File("test-data.txt");
    if (tempFile.exists()) {
      tempFile.delete();
    }
    tempFile = new File("data-sort.txt");
    if (tempFile.exists()) {
      tempFile.delete();
    }
  }

  @ParameterizedTest
  @DisplayName("Sort Big File with long")
  @CsvSource({
      "2",
      "10",
      "100",
      "1_000",
      "1_000_000"
  })
  void sort(int count) throws IOException {
    GeneratorWithHash generatorWithHash = new GeneratorWithHash();

    File dataFile = generatorWithHash.generate("test-data.txt", count);
    long hashCode = generatorWithHash.getHashCode();

    File sortedFile = new Sorter().sortFile(dataFile);

    Assertions.assertTrue(new ValidatorWithSizeAndHash(sortedFile, count, hashCode).isSorted());
  }
}