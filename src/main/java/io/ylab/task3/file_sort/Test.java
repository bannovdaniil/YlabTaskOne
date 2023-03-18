package io.ylab.task3.file_sort;

import java.io.File;
import java.io.IOException;

public class Test {
  private final static int COUNT = 375_000_000; //375_000_000;


  public static void main(String[] args) throws IOException {
    GeneratorWithHash generatorWithHash = new GeneratorWithHash();

    System.out.println("Generate file.");
    File dataFile = generatorWithHash.generate("data.txt", COUNT);

    long hashCode = generatorWithHash.getHashCode();
    System.out.println("HashCode: " + hashCode);
    System.out.print("Check sort: ");
    System.out.println(new ValidatorWithSizeAndHash(dataFile, COUNT, hashCode).isSorted()); // false

    System.out.println("Sort file.");
    long timeStart = System.currentTimeMillis();
    File sortedFile = new Sorter().sortFile(dataFile);
    System.out.println("Sort Time: " + (System.currentTimeMillis() - timeStart));

    System.out.print("Check sort: ");
    System.out.println(new ValidatorWithSizeAndHash(sortedFile, COUNT, hashCode).isSorted()); // true
  }
}
