package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.dao.PersonRepository;
import io.ylab.intensive.lesson04.eventsourcing.dao.PersonRepositoryImpl;
import io.ylab.intensive.lesson04.eventsourcing.model.Person;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    ConnectionFactory connectionFactory = initMQ();
    DataSource dataSource = initDb();
    PersonRepository personRepository = new PersonRepositoryImpl(dataSource);

    PersonApi personApi = new PersonApiImpl(connectionFactory, personRepository);

    personApi.deletePerson(1L);
    personApi.savePerson(1L, "First", "Last", "Middle");
    Person person = personApi.findPerson(1L);
    List<Person> personList = personApi.findAll();
    System.out.println(person);
    System.out.println(personList);
  }

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }

  private static DataSource initDb() throws SQLException {
    String ddl = ""
        + "CREATE TABLE IF NOT EXISTS person (\n"
        + "person_id BIGINT PRIMARY KEY,\n"
        + "first_name VARCHAR,\n"
        + "last_name VARCHAR,\n"
        + "middle_name VARCHAR\n"
        + ")";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(ddl, dataSource);
    return dataSource;
  }
}
