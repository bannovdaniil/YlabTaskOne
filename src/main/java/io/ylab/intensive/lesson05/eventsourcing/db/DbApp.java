package io.ylab.intensive.lesson05.eventsourcing.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.ylab.intensive.lesson05.eventsourcing.Constants;
import io.ylab.intensive.lesson05.eventsourcing.model.Message;
import io.ylab.intensive.lesson05.eventsourcing.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.charset.StandardCharsets;

public class DbApp {
  private final static ObjectMapper objectMapper = new ObjectMapper();
  private final static Logger LOGGER = LoggerFactory.getLogger(DbApp.class);

  public static void main(String[] args) throws Exception {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    // тут пишем создание и запуск приложения работы с БД
    ConnectionFactory connectionFactory = applicationContext.getBean(ConnectionFactory.class);
    PersonRepository personRepository = applicationContext.getBean(PersonRepository.class);

    try (Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel()) {

      channel.exchangeDeclare(Constants.EXCHANGER_NAME, BuiltinExchangeType.DIRECT, true);
      channel.queueDeclare(Constants.COMMAND_QUEUE, true, false, false, null);

      channel.queueBind(Constants.COMMAND_QUEUE, Constants.EXCHANGER_NAME, Constants.ROUTING_KEY);
      LOGGER.info(" [*] Waiting for messages");
      while (!Thread.currentThread().isInterrupted()) {
        GetResponse response = channel.basicGet(Constants.COMMAND_QUEUE, true);
        if (response != null) {
          String messageString = new String(response.getBody(), StandardCharsets.UTF_8);
          LOGGER.info(" [v] Received {}, {}", response.getEnvelope().getRoutingKey(), messageString);
          execResponseCommand(personRepository, messageString);
        } else {
          Thread.sleep(100);
        }
      }
    }
  }

  private static void execResponseCommand(PersonRepository personRepository, String messageString) throws JsonProcessingException {
    Message message = objectMapper.readValue(messageString, Message.class);
    switch (message.getCommand()) {
      case SAVE:
        personRepository.save(message.getPerson());
        break;
      case DELETE:
        personRepository.delete(message.getPersonId());
        break;
      default:
        LOGGER.error("Command not found: {}", message.getCommand());
    }
  }
}
