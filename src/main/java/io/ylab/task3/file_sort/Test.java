package io.ylab.task3.file_sort;

import java.io.File;
import java.io.IOException;

public class Test {
  public static void main(String[] args) throws IOException {
    System.out.println("Generate file.");
    File dataFile = new Generator().generate("data.txt", 1_000_000);//375_000_000);
    System.out.print("Check sort: ");
    System.out.println(new Validator(dataFile).isSorted()); // false
    System.out.println("Sort file.");
    long timeStart = System.currentTimeMillis();
    File sortedFile = new Sorter().sortFile(dataFile);
    System.out.println("Sort Time: " + (System.currentTimeMillis() - timeStart));
    System.out.print("Check sort: ");
    System.out.println(new Validator(sortedFile).isSorted()); // true
  }
}
