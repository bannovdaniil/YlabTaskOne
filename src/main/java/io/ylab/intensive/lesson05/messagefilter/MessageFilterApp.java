package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson05.messagefilter.utils.CensoringMessage;
import io.ylab.intensive.lesson05.messagefilter.utils.FileLoader;
import io.ylab.intensive.lesson05.messagefilter.utils.Messenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class MessageFilterApp {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageFilterApp.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();

        FileLoader fileLoader = applicationContext.getBean(FileLoader.class);
        Messenger messenger = applicationContext.getBean(Messenger.class);
        CensoringMessage censoringMessage = applicationContext.getBean(CensoringMessage.class);

        fileLoader.loadFileToDB("bad_words.txt");

        ConnectionFactory connectionFactory = applicationContext.getBean(ConnectionFactory.class);

        LOGGER.info(" [*] Waiting for messages");

           messenger.purgeOutputQueue();
        while (!Thread.currentThread().isInterrupted()) {
            Optional<String> message = messenger.receiveMessage();
            if (message.isPresent()) {
                System.out.println(message);
                messenger.sendMessage(censoringMessage.replaceBadWords(message.orElse("")));
            }
        }

        messenger.close();
    }

}
