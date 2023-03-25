package io.ylab.intensive.lesson04.eventsourcing.dao;

import io.ylab.intensive.lesson04.eventsourcing.model.Person;

import java.util.List;

public interface PersonRepository {

  public void delete(Long personId);

  public void save(Person person);

  public boolean exists(Long personId);

  public void update(Person person);

  public Person findById(Long personId);

  public List<Person> findAll();

}
