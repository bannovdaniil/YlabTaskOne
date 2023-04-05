package io.ylab.intensive.lesson04.persistentmap;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersistentMapImpl implements PersistentMap {

    private final DataSource dataSource;
    private String name = "";

    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init(String name) {
        this.name = (name == null) ? "" : name;
    }

    @Override
    public boolean containsKey(String key) throws SQLException {
        boolean value = false;
        String sql = "SELECT EXISTS(SELECT 1 FROM persistent_map WHERE map_name = ? AND key = ? LIMIT 1);";
        if (key == null) {
            sql = "SELECT  EXISTS(SELECT 1 FROM persistent_map WHERE map_name = ? AND key IS NULL LIMIT 1);";
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setStatementStringOrNull(statement, 1, this.name);
            if (key != null) {
                statement.setString(2, key);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    value = resultSet.getBoolean(1);
                }
            }
        }
        return value;
    }

    @Override
    public List<String> getKeys() throws SQLException {
        final String sql = "SELECT key FROM persistent_map WHERE map_name = ?;";
        List<String> keyList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setStatementStringOrNull(statement, 1, this.name);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    keyList.add(resultSet.getString(1));
                }
            }
        }
        return keyList;
    }

    @Override
    public String get(String key) throws SQLException {
        String value = null;
        String sql = "SELECT value FROM persistent_map WHERE map_name = ? AND key = ? LIMIT 1;";
        if (key == null) {
            sql = "SELECT value FROM persistent_map WHERE map_name = ? AND key IS NULL LIMIT 1;";
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setStatementStringOrNull(statement, 1, this.name);
            if (key != null) {
                statement.setString(2, key);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    value = resultSet.getString(1);
                }
            }
        }
        return value;
    }

    @Override
    public void remove(String key) throws SQLException {
        String sql = "DELETE FROM persistent_map WHERE map_name = ? AND key = ?;";
        if (key == null) {
            sql = "DELETE FROM persistent_map WHERE map_name = ? AND key IS NULL;";
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setStatementStringOrNull(statement, 1, this.name);
            if (key != null) {
                statement.setString(2, key);
            }

            statement.executeUpdate();
        }
    }

    @Override
    public void put(String key, String value) throws SQLException {
        if (containsKey(key)) {
            remove(key);
        }
        final String sql = "INSERT INTO persistent_map (map_name, key, value) VALUES (?, ?, ?);";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setStatementStringOrNull(statement, 1, this.name);
            setStatementStringOrNull(statement, 2, key);
            setStatementStringOrNull(statement, 3, value);

            statement.executeUpdate();
        }
    }

    @Override
    public void clear() throws SQLException {
        final String sql = "DELETE FROM persistent_map WHERE map_name = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setStatementStringOrNull(statement, 1, this.name);

            statement.executeUpdate();
        }
    }

    private static void setStatementStringOrNull(PreparedStatement statement, int position, String text) throws SQLException {
        if (text == null) {
            statement.setNull(position, Types.VARCHAR);
        } else {
            statement.setString(position, text);
        }
    }
}
