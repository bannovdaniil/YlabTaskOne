package io.ylab.intensive.lesson04.eventsourcing.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
  private String command;
  private Long personId;
  private Person person;

  public Message() {
  }

  public Message(String command, Long personId, Person person) {
    this.command = command;
    this.personId = personId;
    this.person = person;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }
}
