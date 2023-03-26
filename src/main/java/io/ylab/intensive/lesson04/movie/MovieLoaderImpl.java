package io.ylab.intensive.lesson04.movie;

import com.sun.jdi.InvalidTypeException;
import io.ylab.intensive.lesson04.movie.model.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Scanner;

public class MovieLoaderImpl implements MovieLoader {
  private final DataSource dataSource;
  private final Logger LOGGER = LoggerFactory.getLogger(MovieLoaderImpl.class);

  public MovieLoaderImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void loadData(File file) {
    final String sql = "INSERT INTO movie "
        + " (year, length, title, subject, actors, actress, director, popularity, awards) "
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    LOGGER.info("Start load data");
    try (Scanner sc = new Scanner(file, Charset.forName("windows-1252"))) {
      String headerOfFile = sc.hasNext() ? sc.nextLine() : null;
      String typeOfField = sc.hasNext() ? sc.nextLine() : null;

      checkCorrectFileOrThrow(headerOfFile, typeOfField);

      try (Connection connection = dataSource.getConnection();
           PreparedStatement statement = connection.prepareStatement(sql)) {

        long count = 0L;
        connection.setAutoCommit(false);
        while (sc.hasNext()) {
          String movieInfoLine = sc.nextLine().trim();
          count++;

          Movie movie = stringParseToMovie(movieInfoLine);
          if (movie == null) {
            continue;
          }

          setStatementIntOrNull(statement, 1, movie.getYear());
          setStatementIntOrNull(statement, 2, movie.getLength());
          setStatementStringOrNull(statement, 3, movie.getTitle());

          setStatementStringOrNull(statement, 4, movie.getSubject());
          setStatementStringOrNull(statement, 5, movie.getActors());
          setStatementStringOrNull(statement, 6, movie.getActress());

          setStatementStringOrNull(statement, 7, movie.getDirector());
          setStatementIntOrNull(statement, 8, movie.getPopularity());
          setBooleanOrNull(statement, 9, movie.getAwards());

          statement.addBatch();
        }

        try {
          statement.executeBatch();
          connection.commit();
        } catch (BatchUpdateException e) {
          LOGGER.error(e.getMessage());
          connection.rollback();
        }

        connection.setAutoCommit(true);
        LOGGER.info("Data load ({} records) - OK", count);
      } catch (SQLException e) {
        LOGGER.error(e.getMessage());
        throw new RuntimeException(e);
      }

    } catch (InvalidTypeException | IOException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private Movie stringParseToMovie(String movieInfoString) {
    String[] movieInfo = movieInfoString.split(";");
    if (movieInfo.length < 9) {
      return null;
    }
    return new Movie()
        .setYear(setNullOrInt(movieInfo[0]))
        .setLength(setNullOrInt(movieInfo[1]))
        .setTitle(setNullOrString(movieInfo[2]))
        .setSubject(setNullOrString(movieInfo[3]))
        .setActors(setNullOrString(movieInfo[4]))
        .setActress(setNullOrString(movieInfo[5]))
        .setDirector(setNullOrString(movieInfo[6]))
        .setPopularity(setNullOrInt(movieInfo[7]))
        .setAwards(setNullOrBoolean(movieInfo[8]));
  }

  private Boolean setNullOrBoolean(String award) {
    return award.isEmpty() ? null : "Yes".equals(award);
  }

  private Integer setNullOrInt(String num) {
    return num.isEmpty() ? null : Integer.parseInt(num);
  }

  private String setNullOrString(String text) {
    return text.isEmpty() ? null : text;
  }

  private static void setBooleanOrNull(PreparedStatement statement, int position, Boolean bool) throws SQLException {
    if (bool == null) {
      statement.setNull(position, Types.BOOLEAN);
    } else {
      statement.setBoolean(position, bool);
    }
  }

  private static void setStatementIntOrNull(PreparedStatement statement, int position, Integer num) throws SQLException {
    if (num == null) {
      statement.setNull(position, Types.INTEGER);
    } else {
      statement.setInt(position, num);
    }
  }

  private static void setStatementStringOrNull(PreparedStatement statement, int position, String text) throws SQLException {
    if (text == null) {
      statement.setNull(position, Types.VARCHAR);
    } else {
      statement.setString(position, text);
    }
  }

  private void checkCorrectFileOrThrow(String headerOfFile, String typeOfField) throws InvalidTypeException {
    if (headerOfFile == null
        || typeOfField == null
        || (!headerOfFile.startsWith("Year;Length;Title;Subject;Actor;Actress;Director;Popularity;Awards")
        && !typeOfField.startsWith("INT;INT;STRING;CAT;CAT;CAT;CAT;INT;BOOL"))
    ) {
      throw new InvalidTypeException("Неверный тип файла или переданный файл не содержит необходимые поля.");
    }
  }
}
