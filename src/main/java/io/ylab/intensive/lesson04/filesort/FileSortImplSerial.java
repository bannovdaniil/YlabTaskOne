package io.ylab.intensive.lesson04.filesort;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Scanner;

public class FileSortImplSerial implements FileSorter {
  private final DataSource dataSource;

  public FileSortImplSerial(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public File sort(File data) {
    File outFile = new File("data_out_serial.txt");
    serialAppendData(data);
    sortAndSave(outFile);

    return outFile;
  }

  private void serialAppendData(File file) {
    final String sql = "INSERT INTO numbers VALUES (?)";

    try (Scanner sc = new Scanner(file);
         Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      connection.setAutoCommit(true);
      while (sc.hasNext()) {
        long num = sc.nextLong();

        statement.setLong(1, num);
        statement.executeUpdate();
      }

    } catch (FileNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void sortAndSave(File file) {
    final String sqlSort = "SELECT val FROM numbers ORDER BY val DESC;";

    try (PrintWriter pw = new PrintWriter(file);
         Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sqlSort);
      while (resultSet.next()) {
        pw.println(resultSet.getLong("val"));
      }
      pw.flush();
    } catch (FileNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
