package io.ylab.intensive.lesson04.filesort;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Scanner;

public class FileSortImpl implements FileSorter {
  private final DataSource dataSource;
  private final static int BATCH_BLOCK_SIZE = 1_000;

  public FileSortImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  @Override
  public File sort(File data) {
    File outFile = new File("data_out.txt");
    batchAppendData(data);
    sortAndSave(outFile);

    return outFile;
  }

  private void batchAppendData(File file) {
    final String sql = "INSERT INTO numbers VALUES (?)";

    try (Scanner sc = new Scanner(file);
         Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      connection.setAutoCommit(false);
      int count = 0;

      while (sc.hasNext()) {
        long num = sc.nextLong();
        statement.setLong(1, num);
        statement.addBatch();
        if (count++ >= BATCH_BLOCK_SIZE) {
          saveBath(connection, statement);
          count = 0;
        }
      }

      if (count > 0) {
        saveBath(connection, statement);
      }

      connection.setAutoCommit(true);
    } catch (FileNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void saveBath(Connection connection, PreparedStatement statement) throws SQLException {
    try {
      statement.executeBatch();
      connection.commit();
    } catch (BatchUpdateException e) {
      System.out.println(e.getMessage());
      connection.rollback();
    }
  }

  private void sortAndSave(File file) {
    final String sqlSort = "SELECT val FROM numbers ORDER BY val DESC;";

    try (PrintWriter pw = new PrintWriter(file);
         Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sqlSort)) {
      while (resultSet.next()) {
        pw.println(resultSet.getLong("val"));
      }
      pw.flush();
    } catch (FileNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
