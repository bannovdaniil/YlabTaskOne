package io.ylab.intensive.lesson04.eventsourcing.db;

import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {
  private final DataSource dataSource;

  public PersonRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void delete(Long personId) {
    final String sql = "DELETE FROM FROM person WHERE person_id = ?;";

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, personId);
      statement.execute(sql);
    } catch (
        SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void save(Person person) {
    final String sql = "INSERT INTO person (first_name, last_name, middle_name) VALUES(?, ?, ?);";

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, person.getName());
      statement.setString(2, person.getLastName());
      statement.setString(3, person.getMiddleName());
      statement.execute(sql);
    } catch (
        SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean exists(Long personId) {
    final String sql = "SELECT EXISTS(SELECT 1 FROM person WHERE person_id = ? LIMIT 1);";
    boolean exists = false;

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, personId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        exists = resultSet.getBoolean(1);
      }
    } catch (
        SQLException e) {
      throw new RuntimeException(e);
    }
    return exists;
  }

  public void update(Person person) {
    final String sql = "UPDATE person SET first_name = ?, last_name = ?, middle_name = ? WHERE person_id = ?;";

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, person.getName());
      statement.setString(2, person.getLastName());
      statement.setString(3, person.getMiddleName());
      statement.setLong(4, person.getId());

      statement.executeUpdate(sql);
    } catch (
        SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Person findById(Long personId) {
    final String sql = "SELECT person_id, first_name, last_name, middle_name"
        + " FROM person WHERE person_id = ?;";

    Person person = null;

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, personId);

      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        person = new Person(
            resultSet.getLong("person_id"),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"),
            resultSet.getString("middleName")
        );
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return person;
  }

  public List<Person> findAll() {
    final String sql = "SELECT person_id, first_name, last_name, middle_name FROM person;";
    List<Person> personList = new ArrayList<>();

    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        personList.add(
            new Person(
                resultSet.getLong("person_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("middleName")
            )
        );
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return personList;
  }

}
