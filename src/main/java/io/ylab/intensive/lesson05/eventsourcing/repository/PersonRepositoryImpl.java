package io.ylab.intensive.lesson05.eventsourcing.repository;

import io.ylab.intensive.lesson05.eventsourcing.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepositoryImpl implements PersonRepository {
  private final DataSource dataSource;
  private final static Logger LOGGER = LoggerFactory.getLogger(PersonRepositoryImpl.class);

  @Autowired
  public PersonRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void delete(Long personId) {
    LOGGER.info("delete({})", personId);
    if (!exists(personId)) {
      LOGGER.info("Person id={} not found.", personId);

      return;
    }
    final String sql = "DELETE FROM person WHERE person_id = ?;";

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, personId);
      int rowsAffected = statement.executeUpdate();
      LOGGER.info("delete: {} records.", rowsAffected);
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public void save(Person person) {
    if (person.getId() != null && exists(person.getId())) {
      update(person);
      return;
    }
    if (person.getId() == null) {
      person.setId(getNextId());
    }
    LOGGER.info("Save: {}", person);
    final String sql = "INSERT INTO person (person_id, first_name, last_name, middle_name) VALUES(?, ?, ?, ?);";

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, person.getId());
      statement.setString(2, person.getName());
      statement.setString(3, person.getLastName());
      statement.setString(4, person.getMiddleName());
      statement.executeUpdate();
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean exists(Long personId) {
    LOGGER.info("Call exists({})", personId);
    if (personId == null) {
      LOGGER.info("Person ID={} not found", personId);
      return false;
    }
    final String sql = "SELECT EXISTS(SELECT 1 FROM person WHERE person_id = ? LIMIT 1);";
    boolean exists = false;

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, personId);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          exists = resultSet.getBoolean(1);
        }
      }
      LOGGER.info("Person id = {} exists = {}.", personId, exists);
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
    return exists;
  }

  @Override
  public void update(Person person) {
    LOGGER.info("Update: {}", person);
    if (person.getId() == null) {
      save(person);
      return;
    }
    final String sql = "UPDATE person SET first_name = ?, last_name = ?, middle_name = ? WHERE person_id = ?;";

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, person.getName());
      statement.setString(2, person.getLastName());
      statement.setString(3, person.getMiddleName());
      statement.setLong(4, person.getId());

      statement.executeUpdate();
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public Person findById(Long personId) {
    LOGGER.info("findById({}))", personId);
    final String sql = "SELECT person_id, first_name, last_name, middle_name"
        + " FROM person WHERE person_id = ?;";

    Person person = null;

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, personId);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          person = new Person(
              resultSet.getLong("person_id"),
              resultSet.getString("first_name"),
              resultSet.getString("last_name"),
              resultSet.getString("middle_name")
          );
        }
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
    return person;
  }

  @Override
  public List<Person> findAll() {
    LOGGER.info("FindAll");
    final String sql = "SELECT person_id, first_name, last_name, middle_name FROM person;";
    List<Person> personList = new ArrayList<>();

    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(sql)) {
        while (resultSet.next()) {
          personList.add(
              new Person(
                  resultSet.getLong("person_id"),
                  resultSet.getString("first_name"),
                  resultSet.getString("last_name"),
                  resultSet.getString("middle_name")
              )
          );
        }
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }

    return personList;
  }

  public Long getNextId() {
    LOGGER.info("getNextId.");
    final String sql = "SELECT MAX(person_id) as max_id FROM person;";

    long maxId = 0L;

    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {

      if (resultSet.next()) {
        maxId = resultSet.getLong("max_id");
      }
      LOGGER.info("Last ID = {}", maxId);
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
    return (maxId + 1L);
  }
}
