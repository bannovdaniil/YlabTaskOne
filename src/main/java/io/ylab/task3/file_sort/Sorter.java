package io.ylab.task3.file_sort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * 1_048_575 = размер файла инициализации 21мб, быстрее на 20%
 * 524_288 = размер файла инициализации 11мб, быстрее на 10%
 * 262_144 = размер файла инициализации 5мб, быстрее на 10%
 */
public class Sorter {
  private final static int BLOCK_SIZE = 1_048_575;

  public File sortFile(File dataFile) throws IOException {
    List<File> tempFileList = new ArrayList<>();
    try (Scanner scanner = new Scanner(new FileInputStream(dataFile))) {
      initSortBlocks(tempFileList, scanner);

      File resultFile = new File("data-sort.txt");
      if (resultFile.exists()) {
        resultFile.delete();
      }

      if (tempFileList.size() > 0) {
        File tempSortedFile = mergeSorted(tempFileList);
        Files.move(tempSortedFile.toPath(), resultFile.toPath());
      }
      return resultFile;
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  private void initSortBlocks(List<File> tempFileList, Scanner scanner) throws IOException {
    List<Long> block = new ArrayList<>();
    while (scanner.hasNextLong()) {
      long current = scanner.nextLong();
      block.add(current);
      if (block.size() >= BLOCK_SIZE) {
        Collections.sort(block);
        tempFileList.add(saveBlockToTempFile(block));
        block = new ArrayList<>();
      }
    }

    if (block.size() > 0) {
      Collections.sort(block);
      tempFileList.add(saveBlockToTempFile(block));
    }
  }

  private File mergeSorted(List<File> tempFileList) throws IOException {
    while (tempFileList.size() > 1) {
      List<File> newTempFileList = new ArrayList<>();
      for (int i = 0; i < tempFileList.size(); i += 2) {
        File file1 = tempFileList.get(i);
        if (i + 1 < tempFileList.size()) {
          File file2 = tempFileList.get(i + 1);
          newTempFileList.add(mergeAndSortTwoFile(file1, file2));
          file1.delete();
          file2.delete();
        } else {
          newTempFileList.add(file1);
        }
      }
      tempFileList = newTempFileList;
    }
    return tempFileList.get(0);
  }

  private File mergeAndSortTwoFile(File f1, File f2) throws IOException {
    File tempFile = Files.createTempFile("data-sort-", ".temp").toFile();
    Long longFromFile1 = null;
    Long longFromFile2 = null;
    try (PrintWriter pw = new PrintWriter(tempFile);
         Scanner sc1 = new Scanner(f1);
         Scanner sc2 = new Scanner(f2)) {
      do {
        if (longFromFile1 == null) {
          longFromFile1 = getLongFromFile(sc1);
        }
        if (longFromFile2 == null) {
          longFromFile2 = getLongFromFile(sc2);
        }

        if (longFromFile1 != null && longFromFile2 != null) {
          int select = longFromFile1.compareTo(longFromFile2);
          if (select == 0) {
            pw.println(longFromFile1);
            pw.println(longFromFile2);
            longFromFile1 = getLongFromFile(sc1);
            longFromFile2 = getLongFromFile(sc2);
          } else if (select < 0) {
            pw.println(longFromFile1);
            longFromFile1 = getLongFromFile(sc1);
          } else {
            pw.println(longFromFile2);
            longFromFile2 = getLongFromFile(sc2);
          }
        }
      } while (longFromFile1 != null && longFromFile2 != null);

      saveLastIfFirstIsNull(longFromFile1, longFromFile2, pw, sc2);
      saveLastIfFirstIsNull(longFromFile2, longFromFile1, pw, sc1);
      pw.flush();
    }
    return tempFile;
  }

  private void saveLastIfFirstIsNull(Long nullLong, Long saveLong, PrintWriter pw, Scanner sc) {
    if (nullLong == null) {
      while (saveLong != null) {
        pw.println(saveLong);
        saveLong = getLongFromFile(sc);
      }
    }
  }

  private Long getLongFromFile(Scanner sc) {
    return sc.hasNextLong() ? sc.nextLong() : null;
  }

  private File saveBlockToTempFile(List<Long> block) throws IOException {
    File tempFile = Files.createTempFile("data-sort-", ".temp").toFile();
    try (PrintWriter pw = new PrintWriter(tempFile)) {
      for (var num : block) {
        pw.println(num);
      }
      pw.flush();
    }
    return tempFile;
  }
}
