package io.ylab.intensive.lesson05.messagefilter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CensoringMessage {
    private final DataSource dataSource;
    private final Connection connection;
    private final static Logger LOGGER = LoggerFactory.getLogger(CensoringMessage.class);
    private final StringBuilder wordWithStar = new StringBuilder();
    private final String splitterChars = "[\\?\\.!,\\s]";

    public CensoringMessage(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        connection = dataSource.getConnection();
    }

    public String replaceBadWords(String message) {
        if (message == null || message.isBlank()) {
            return message;
        }
        String[] words = message.split(splitterChars);
        StringBuilder sb = new StringBuilder(message);
        for (var word : words) {
            if (findInDB(word)) {
                String newWord = replaceWithStars(word);
                int position = message.indexOf(word);
                sb.replace(position, position + word.length(), newWord);
            }
        }
        return sb.toString();
    }

    private String replaceWithStars(String word) {
        int repeatCount = word.length() - 2;
        wordWithStar.setLength(0);
        wordWithStar.append(word.charAt(0));
        wordWithStar.append("*".repeat(repeatCount));
        wordWithStar.append(word.charAt(repeatCount + 1));
        return wordWithStar.toString();
    }

    private boolean findInDB(String word) {
        if (word == null || word.isBlank()) {
            return false;
        }
        final String sql = "SELECT EXISTS(SELECT 1 FROM bad_words WHERE LOWER(word) = LOWER(?) LIMIT 1);";
        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exists = resultSet.getBoolean(1);
                }
            }
            if (exists) {
                LOGGER.info("Word = {} found.", word);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return exists;
    }
}
