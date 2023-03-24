package io.ylab.intensive.lesson04.movie;

import com.sun.jdi.InvalidTypeException;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Scanner;

public class MovieLoaderImpl implements MovieLoader {
  private final DataSource dataSource;

  public MovieLoaderImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void loadData(File file) {
    final String sql = "INSERT INTO movie " + " (year, length, title, subject, actors, actress, director, popularity, awards) " + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Scanner sc = new Scanner(file, Charset.forName("windows-1252"))) {
      String headerOfFile = sc.hasNext() ? sc.nextLine() : null;
      String typeOfField = sc.hasNext() ? sc.nextLine() : null;

      checkCorrectFileOrThrow(headerOfFile, typeOfField);

      try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

        connection.setAutoCommit(false);
        while (sc.hasNext()) {
          String[] movieInfo = sc.nextLine().split(";");

          String year = movieInfo[0];
          String length = movieInfo[1];
          String title = movieInfo[2];
          String subject = movieInfo[3];
          String actors = movieInfo[4];
          String actress = movieInfo[5];
          String director = movieInfo[6];
          String popularity = movieInfo[7];
          String awards = movieInfo[8];

          setStatementIntOrNull(statement, 1, year);
          setStatementIntOrNull(statement, 2, length);
          setStatementStringOrNull(statement, 3, title);
          setStatementStringOrNull(statement, 4, subject);
          setStatementStringOrNull(statement, 5, actors);
          setStatementStringOrNull(statement, 6, actress);
          setStatementStringOrNull(statement, 7, director);
          setStatementIntOrNull(statement, 8, popularity);
          setBooleanOrNull(statement, 9, awards);

          statement.addBatch();
        }

        try {
          statement.executeBatch();
          connection.commit();
        } catch (BatchUpdateException e) {
          System.out.println(e.getMessage());
          connection.rollback();
        }

        connection.setAutoCommit(true);
      } catch (SQLException e) {
        System.out.println(e.getMessage());
        throw new RuntimeException(e);
      }

    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e);
    } catch (InvalidTypeException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void setBooleanOrNull(PreparedStatement statement, int position, String bool) throws SQLException {
    if (bool.isEmpty()) {
      statement.setNull(position, Types.BOOLEAN);
    } else {
      statement.setBoolean(position, "Yes".equals(bool));
    }
  }

  private static void setStatementIntOrNull(PreparedStatement statement, int position, String num) throws SQLException {
    if (num.isEmpty()) {
      statement.setNull(position, Types.INTEGER);
    } else {
      statement.setInt(position, Integer.parseInt(num));
    }
  }

  private static void setStatementStringOrNull(PreparedStatement statement, int position, String text) throws SQLException {
    if (text.isEmpty()) {
      statement.setNull(position, Types.VARCHAR);
    } else {
      statement.setString(position, text);
    }
  }

  private void checkCorrectFileOrThrow(String headerOfFile, String typeOfField) throws InvalidTypeException {
    if (headerOfFile == null
        && typeOfField == null
        && !headerOfFile.startsWith("Year;Length;Title;Subject;Actor;Actress;Director;Popularity;Awards")
        && !typeOfField.startsWith("INT;INT;STRING;CAT;CAT;CAT;CAT;INT;BOOL")
    ) {
      throw new InvalidTypeException("Неверный тип файла или переданный файл не содержит необходимые поля.");
    }
  }
}
