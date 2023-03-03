package io.ylab.task1;

/**
 * MultTable. На вход ничего не подается, необходимо распечатать таблицу умножения чисел от 1 до 9 (включая)
 */
public class MultTable {
  public static void main(String[] args) {
    StringBuilder multipleTableOutput = new StringBuilder();

    for (int i = 1; i <= 9; i++) {
      for (int j = 1; j <= 9; j++) {
        multipleTableOutput.append(String.format("%d x %d = %d", i, j, i * j))
            .append(System.lineSeparator());
      }
    }

    System.out.print(multipleTableOutput);
  }
}