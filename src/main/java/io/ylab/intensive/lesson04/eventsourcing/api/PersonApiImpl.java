package io.ylab.intensive.lesson04.eventsourcing.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.eventsourcing.Constants;
import io.ylab.intensive.lesson04.eventsourcing.dao.PersonRepository;
import io.ylab.intensive.lesson04.eventsourcing.model.Command;
import io.ylab.intensive.lesson04.eventsourcing.model.Message;
import io.ylab.intensive.lesson04.eventsourcing.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class PersonApiImpl implements PersonApi {
  private final ConnectionFactory connectionFactory;
  private final PersonRepository personRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final static Logger LOGGER = LoggerFactory.getLogger(PersonApiImpl.class);

  public PersonApiImpl(ConnectionFactory connectionFactory, PersonRepository personRepository) {
    this.connectionFactory = connectionFactory;
    this.personRepository = personRepository;
  }

  @Override
  public void deletePerson(Long personId) {
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {

      String jsonMessage = getJson(Command.DELETE, personId, null);

      sendMessageToConsumer(channel, jsonMessage);
    } catch (IOException | TimeoutException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public void savePerson(Long personId, String firstName, String lastName, String middleName) {
    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {

      Person person = new Person(personId, firstName, lastName, middleName);
      String jsonMessage = getJson(Command.SAVE, personId, person);

      sendMessageToConsumer(channel, jsonMessage);
    } catch (IOException | TimeoutException e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public Person findPerson(Long personId) {
    return personRepository.findById(personId);
  }

  @Override
  public List<Person> findAll() {
    return personRepository.findAll();
  }

  private static void sendMessageToConsumer(Channel channel, String jsonMessage) throws IOException {
    channel.exchangeDeclare(Constants.EXCHANGER_NAME, BuiltinExchangeType.DIRECT);
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, Constants.EXCHANGER_NAME, Constants.ROUTING_KEY);
    channel.basicPublish(Constants.EXCHANGER_NAME,
        Constants.ROUTING_KEY,
        null,
        jsonMessage.getBytes(StandardCharsets.UTF_8));
    LOGGER.info("[v] Send: {}: {}", Constants.ROUTING_KEY, jsonMessage);
  }

  private String getJson(Command command, Long personId, Person person) throws JsonProcessingException {
    return objectMapper.writeValueAsString(new Message(command, personId, person));
  }
}
