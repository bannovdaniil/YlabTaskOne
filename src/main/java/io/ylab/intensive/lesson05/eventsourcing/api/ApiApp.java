package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.model.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    io.ylab.intensive.lesson05.eventsourcing.api.PersonApi personApi = applicationContext.getBean(PersonApi.class);
    // пишем взаимодействие с PersonApi

    System.out.println("Send to broker 7 persons.");
    for (long i = 1L; i <= 7L; i++) {
      personApi.savePerson(i, "First" + i, "Last" + i, "Middle" + i);
    }
    System.out.println("Wait records in dataBase.");
    List<Person> personList = waitPersonList(personApi, 7);
    System.out.println("Size = " + personList.size());
    System.out.println("personList = " + personList);

    System.out.println("Find user by ID = 2L");
    System.out.println(personApi.findPerson(2L));

    System.out.println("Delete even and Edit Odd.");
    long lastDeletedId = 0;
    for (Person person : personList) {
      if (person.getId() % 2 == 0) {
        personApi.deletePerson(person.getId());
        lastDeletedId = person.getId();
      } else {
        person.setName(person.getName().replace("First", "EditName" + person.getId()));
        person.setLastName("Робот" + person.getId());
        person.setMiddleName("Петрович" + person.getId());
        personApi.savePerson(person.getId(), person.getName(), person.getLastName(), person.getMiddleName());
      }
    }

    System.out.println("Wait for deleted ID = " + lastDeletedId + " from DB.");
    waitDeletePerson(personApi, lastDeletedId);

    System.out.println("Attempt delete record again:");
    personApi.deletePerson(lastDeletedId);
    System.out.println("Find user by ID = " + lastDeletedId);
    System.out.println(personApi.findPerson(lastDeletedId));

    System.out.println("Find user by ID = 2L");
    System.out.println(personApi.findPerson(2L));

    System.out.println("Find user by ID = 3L");
    System.out.println(personApi.findPerson(3L));

  }

  private static void waitDeletePerson(PersonApi personApi, long personId) throws InterruptedException {
    while (!Thread.currentThread().isInterrupted()) {
      Person person = personApi.findPerson(personId);
      if (person == null) {
        break;
      }
      Thread.sleep(1000);
    }
  }

  private static List<Person> waitPersonList(PersonApi personApi, int size) throws InterruptedException {
    List<Person> personList = new ArrayList<>();
    while (!Thread.currentThread().isInterrupted()) {
      personList = personApi.findAll();
      if (personList.size() >= size) {
        break;
      }
      Thread.sleep(1000);
    }
    return personList;
  }
}
