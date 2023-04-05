package io.ylab.intensive.lesson04.eventsourcing.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private Command command;
    private Long personId;
    private Person person;

    public Message() {
    }

    public Message(Command command, Long personId, Person person) {
        this.command = command;
        this.personId = personId;
        this.person = person;
    }

    public Command getCommand() {
        return command;
    }

    public Long getPersonId() {
        return personId;
    }

    public Person getPerson() {
        return person;
    }
}
