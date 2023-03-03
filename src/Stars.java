import java.util.Scanner;

/**
 * Stars. Программе передается 3 параметра: количество строк, количество столбцов, произвольный симов.
 * Необходимо вывести фигуру, состоящую из заданного списка строк и заданного количества столбцов,
 * и каждый элемент в которой равен указанному символу.
 */
public class Stars {
  public static void main(String[] args) {
    try (Scanner scanner = new Scanner(System.in)) {
      int n = scanner.nextInt();
      int m = scanner.nextInt();
      String template = scanner.next();

      StringBuilder figureOut = new StringBuilder();
      while (n > 0) {
        figureOut.append(template.repeat(m)).append(System.lineSeparator());
        n--;
      }
      System.out.println(figureOut);
    }
  }
}
