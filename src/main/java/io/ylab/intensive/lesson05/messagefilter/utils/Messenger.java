package io.ylab.intensive.lesson05.messagefilter.utils;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class Messenger {
    private final ConnectionFactory connectionFactory;
    //    private final Connection connection;
    private final Channel channelIn;
    private final Channel channelOut;

    private final Logger LOGGER = LoggerFactory.getLogger(Messenger.class);
    private final String EXCHANGER_NAME = "FILTER_MESSAGE";
    private final String IN_QUEUE = "input";
    private final String OUT_QUEUE = "output";

    @Autowired
    public Messenger(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
        this.connectionFactory = connectionFactory;
        Connection connection = connectionFactory.newConnection();

        channelIn = connection.createChannel();
        channelIn.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.DIRECT, true);
        channelIn.queueDeclare(IN_QUEUE, true, false, false, null);

        channelOut = connection.createChannel();
        channelOut.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.DIRECT, true);
        channelOut.queueDeclare(OUT_QUEUE, true, false, false, null);
    }

    public void sendMessage(String message) {
        try {
            channelOut.queueBind(OUT_QUEUE, EXCHANGER_NAME, OUT_QUEUE);
            channelOut.basicPublish(EXCHANGER_NAME,
                    OUT_QUEUE,
                    null,
                    message.getBytes(StandardCharsets.UTF_8));
            LOGGER.info("[v] Send: {}: {}", OUT_QUEUE, message);
        } catch (IOException e) {
            LOGGER.error("Error sending: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void purgeOutputQueue() {
        try {
            channelOut.queuePurge(OUT_QUEUE);
            LOGGER.info("[v] Purge: {}: {}", OUT_QUEUE);
        } catch (Exception e) {
            LOGGER.error("Error purge: {}", e.getMessage());
        }
    }

    public void purgeInputQueue() {
        try {
            channelOut.queuePurge(IN_QUEUE);
            LOGGER.info("[v] Purge: {}: {}", IN_QUEUE);
        } catch (Exception e) {
            LOGGER.error("Error purge: {}", e.getMessage());
        }
    }

    public void prepareMessage(String message) {
        try {
            channelOut.queueBind(IN_QUEUE, EXCHANGER_NAME, IN_QUEUE);
            channelOut.basicPublish(EXCHANGER_NAME,
                    IN_QUEUE,
                    null,
                    message.getBytes(StandardCharsets.UTF_8));
            LOGGER.info("[v] Send: {}: {}", IN_QUEUE, message);
        } catch (IOException e) {
            LOGGER.error("Error sending: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Optional<String> receiveMessage() {
        Optional<String> messageString = Optional.empty();
        GetResponse response = null;
        try {
            channelIn.queueBind(IN_QUEUE, EXCHANGER_NAME, IN_QUEUE);
            response = channelIn.basicGet(IN_QUEUE, true);
            if (response != null) {
                messageString = Optional.of(new String(response.getBody(), StandardCharsets.UTF_8));
                LOGGER.info(" [v] Received {}, {}", response.getEnvelope().getRoutingKey(), messageString);
            }
        } catch (IOException e) {
            LOGGER.error("Error receiving: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return messageString;
    }

    public void close() {
        try {
            channelIn.close();
            channelOut.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
