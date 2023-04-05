package io.ylab.intensive.lesson05.eventsourcing.repository;

import io.ylab.intensive.lesson05.eventsourcing.model.Person;

import java.util.List;

public interface PersonRepository {

  void delete(Long personId);

  void save(Person person);

  boolean exists(Long personId);

  void update(Person person);

  Person findById(Long personId);

  List<Person> findAll();

}
