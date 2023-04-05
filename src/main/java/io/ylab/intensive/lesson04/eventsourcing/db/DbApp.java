package io.ylab.intensive.lesson04.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Constants;
import io.ylab.intensive.lesson04.eventsourcing.dao.PersonRepository;
import io.ylab.intensive.lesson04.eventsourcing.dao.PersonRepositoryImpl;
import io.ylab.intensive.lesson04.eventsourcing.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class DbApp {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static Logger LOGGER = LoggerFactory.getLogger(DbApp.class);

    public static void main(String[] args) throws Exception {
        DataSource dataSource = initDb();
        ConnectionFactory connectionFactory = initMQ();
        PersonRepository personRepository = new PersonRepositoryImpl(dataSource);

        // тут пишем создание и запуск приложения работы с БД
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
                Thread.sleep(100);
            }
        }
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }

    private static DataSource initDb() throws SQLException {
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
        return dataSource;
    }
}
