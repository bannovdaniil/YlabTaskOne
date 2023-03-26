package io.ylab.intensive.lesson04.eventsourcing.dao;

import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.eventsourcing.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;

class PersonRepositoryImplTest {
  private Person person1;
  private Person person2;
  private Person person7;
  private Person personNull;

  private PersonRepositoryImpl personRepository;
  private PrintStream systemErr;
  private ByteArrayOutputStream testErr;

  @BeforeEach
  void setUp() throws SQLException {
    person1 = new Person(1L, "first1", "last1", "middle1");
    person2 = new Person(2L, "first2", "last2", "middle2");
    person7 = new Person(7L, "first7", "last7", "middle7");
    personNull = new Person(null, "firstN", "lastN", "middleN");

    String ddl = ""
        + "DROP TABLE IF EXISTS person;"
        + "CREATE TABLE IF NOT EXISTS person (\n"
        + "person_id BIGINT PRIMARY KEY,\n"
        + "first_name VARCHAR,\n"
        + "last_name VARCHAR,\n"
        + "middle_name VARCHAR\n"
        + ")";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(ddl, dataSource);

    personRepository = new PersonRepositoryImpl(dataSource);

    systemErr = System.err;
    testErr = new ByteArrayOutputStream();
    System.setErr(new PrintStream(testErr));
  }

  @AfterEach
  void tearDown() {
    System.setErr(systemErr);
  }

  @Test
  void delete() {
    long expect = 1L;

    personRepository.delete(1L);

    String logMessages = testErr.toString();
    System.setErr(systemErr);
    System.err.print(logMessages);
    Assertions.assertTrue(logMessages.contains("Person id=1 not found."));

    personRepository.save(person1);
    Person resultPerson = personRepository.findById(1L);

    Assertions.assertEquals(expect, resultPerson.getId());

    personRepository.delete(1L);

    resultPerson = personRepository.findById(1L);

    Assertions.assertNull(resultPerson);
  }

  @Test
  void save() {
    long expect = 2L;

    personRepository.save(person2);
    Person resultPerson = personRepository.findById(2L);

    Assertions.assertEquals(expect, resultPerson.getId());
  }

  @Test
  void exists() {
    long expect = 2L;

    Assertions.assertFalse(personRepository.exists(expect));

    personRepository.save(person2);

    Assertions.assertTrue(personRepository.exists(expect));
  }

  @Test
  void update() {
    Person expect = new Person(1L, "editFirst1", "editLast1", "EditMiddle1");

    personRepository.save(person1);

    Assertions.assertNotEquals(expect, personRepository.findById(1L));

    personRepository.save(expect);

    Person result = personRepository.findById(1L);

    Assertions.assertEquals(expect.getId(), result.getId());
    Assertions.assertEquals(expect.getName(), result.getName());
    Assertions.assertEquals(expect.getLastName(), result.getLastName());
    Assertions.assertEquals(expect.getMiddleName(), result.getMiddleName());
  }

  @Test
  void findById() {
    Person expect = personNull;

    personRepository.save(expect);

    Person result = personRepository.findById(1L);

    Assertions.assertEquals(expect.getId(), result.getId());
    Assertions.assertEquals(expect.getName(), result.getName());
    Assertions.assertEquals(expect.getLastName(), result.getLastName());
    Assertions.assertEquals(expect.getMiddleName(), result.getMiddleName());
  }

  @Test
  void findAll() {
    int expect = 3;

    personRepository.save(person1);
    personRepository.save(person2);
    personRepository.save(personNull);

    List<Person> result = personRepository.findAll();

    Assertions.assertEquals(expect, result.size());
  }

  @Test
  void getNextId() {

    long result = personRepository.getNextId();

    Assertions.assertEquals(1L, result);

    personRepository.save(person1);
    result = personRepository.getNextId();

    Assertions.assertEquals(2L, result);

    personRepository.save(person7);
    result = personRepository.getNextId();

    Assertions.assertEquals(8L, result);


    personRepository.save(personNull);
    result = personRepository.getNextId();

    Assertions.assertEquals(9L, result);
  }
}