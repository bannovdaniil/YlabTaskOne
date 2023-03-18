package io.ylab.task3.file_sort;

import java.io.File;
import java.io.IOException;

public class Test {
  public static void main(String[] args) throws IOException {
    System.out.println("Generate file.");
    File dataFile = new Generator().generate("data.txt", 375_000_000);
    System.out.print("Check sort: ");
    System.out.println(new Validator(dataFile).isSorted()); // false
    System.out.println("Sort file.");
    File sortedFile = new Sorter().sortFile(dataFile);
    System.out.print("Check sort: ");
    System.out.println(new Validator(sortedFile).isSorted()); // true
  }
}
