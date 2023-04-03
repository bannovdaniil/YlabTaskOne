package io.ylab.intensive.lesson05.messagefilter;

import io.ylab.intensive.lesson05.messagefilter.utils.FileLoader;
import io.ylab.intensive.lesson05.messagefilter.utils.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageGeneratorApp {
    private final static Random random = new Random();

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        FileLoader fileLoader = applicationContext.getBean(FileLoader.class);
        Messenger messenger = applicationContext.getBean(Messenger.class);

        fileLoader.loadFileToDB("bad_words.txt");
        List<String> badWordList = getBadWord(dataSource);

        messenger.purgeInputQueue();
        for (int i = 0; i < 30; i++) {
            String message = generateMessage(badWordList,
                    random.nextInt(5) + 2,
                    2,
                    30);
            System.out.println("message = " + message);
            messenger.prepareMessage(message);
        }

        String message = "eблантий, eблантий, eблантий2, 2eблантий+ eблантий! фывафыва елдак\nелдак\n!eблантий! Eблантий EблаНтий eблантиЙ eблантий";
        messenger.prepareMessage(message);


        System.out.println("badWordList = " + badWordList);
        messenger.close();
    }

    private static String randomCase(String word) {
        String first = word.substring(0, 1);
        String last = word.substring(word.length() - 1);
        if (random.nextInt(2) == 1) {
            first = first.toUpperCase();
        }
        if (random.nextInt(2) == 1) {
            last = last.toUpperCase();
        }
        return first + word.substring(1, word.length() - 1) + last;
    }

    private static String generateMessage(List<String> badWordList, int countBadWord, int minWordInSentence, int maxWordInSentence) {
        StringBuilder randomMessage = new StringBuilder();
        int size = random.nextInt(minWordInSentence) + (maxWordInSentence - minWordInSentence);
        for (int i = 0; i < size; i++) {
            randomMessage.append(generateWord()).append(" ");
        }
        int position = 0;
        for (int i = 0; i < countBadWord; i++) {
            String word = randomCase(badWordList.get(random.nextInt(badWordList.size() - 1)));
            position = insertWord(word, randomMessage, position) + word.length() + 2;
        }

        return randomMessage.toString().trim();
    }

    private static int insertWord(String word, StringBuilder randomMessage, int position) {
        StringBuilder sbWord = new StringBuilder(" ").append(word);
        position = position + random.nextInt(randomMessage.length() - position);
        switch (random.nextInt(7)) {
            case 0:
                randomMessage.insert(position, sbWord.append("не то СЛОВО"));
                break;
            case 1:
                randomMessage.insert(position, sbWord.insert(1, "ДОБАВОЧКА слова"));
                break;
            case 2:
                randomMessage.insert(position, sbWord.append("?"));
                break;
            case 3:
                randomMessage.insert(position, sbWord.append("!"));
                break;
            case 4:
                randomMessage.insert(position, sbWord.append("."));
                break;
            case 5:
                randomMessage.insert(position, sbWord.append(","));
                break;
            default:
                randomMessage.insert(position, sbWord.append(" "));
                break;
        }
        return position;
    }

    private static StringBuilder generateWord() {
        StringBuilder randomWord = new StringBuilder();
        for (int i = 0; i < random.nextInt(8) + 1; i++) {
            if (i % 4 == 0) {
                randomWord.append((char) ('А' + random.nextInt(30)));
            } else if (i % 3 == 0) {
                randomWord.append((char) ('a' + random.nextInt(30)));
            } else {
                randomWord.append((char) ('\u0430' + random.nextInt(24)));
            }
        }
        return randomWord;
    }

    private static List<String> getBadWord(@Autowired DataSource dataSource) {
        List<String> wordList = new ArrayList<>();
        String sql = "SELECT word FROM bad_words;";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    wordList.add(resultSet.getString("word"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return wordList;
    }
}
