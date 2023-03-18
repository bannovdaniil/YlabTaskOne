package io.ylab.task3.file_sort;

import java.io.File;
import java.io.IOException;

public class Test {
  private final static int COUNT = 375_000_000;


  public static void main(String[] args) throws IOException {
    System.out.println("Generate file.");
    File dataFile = new Generator().generate("data.txt", COUNT);

    System.out.print("Check sort: ");
    System.out.println(new Validator(dataFile).isSorted()); // false

    long timeStart = System.currentTimeMillis();
    System.out.println("Sort file.");
    File sortedFile = new Sorter().sortFile(dataFile);
    System.out.println("time: " + (System.currentTimeMillis() - timeStart));

    System.out.print("Check sort: ");
    System.out.println(new Validator(sortedFile).isSorted()); // true
  }
}
