package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {
  private final DataSource dataSource;
  private final static String SQL_FORMAT = "SELECT %s FROM %s";

  @Autowired
  public SQLQueryBuilderImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public String queryForTable(String tableName) throws SQLException {
    if (!getTables().contains(tableName)) {
      return null;
    }
    StringJoiner sb = new StringJoiner(", ");
    try (Connection connection = dataSource.getConnection()) {

      DatabaseMetaData databaseMetaData = connection.getMetaData();
      try (ResultSet resultSet = databaseMetaData.getColumns(
          null,
          null,
          tableName,
          "%")
      ) {
        while (resultSet.next()) {
          sb.add(resultSet.getString("COLUMN_NAME"));
        }
      }
    }
    if (sb.length() == 0) {
      System.out.println("таблица без столбцов");
      return null;
    }
    return String.format(SQL_FORMAT, sb, tableName);
  }

  @Override
  public List<String> getTables() throws SQLException {
    List<String> tableList = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {

      DatabaseMetaData databaseMetaData = connection.getMetaData();
      try (ResultSet resultSet = databaseMetaData.getTables(
          null,
          null,
          "%",
          new String[]{"TABLE", "SYSTEM TABLE"})
      ) {
        while (resultSet.next()) {
          tableList.add(resultSet.getString("TABLE_NAME"));
        }
      }
    }
    return tableList;
  }
}
