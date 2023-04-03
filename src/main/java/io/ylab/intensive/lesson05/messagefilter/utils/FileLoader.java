package io.ylab.intensive.lesson05.messagefilter.utils;

import io.ylab.intensive.lesson05.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

@Component
public class FileLoader {
    private final Logger LOGGER = LoggerFactory.getLogger(FileLoader.class);

    private final DataSource dataSource;
    private final String sql = "INSERT INTO bad_words (word) VALUES (?)";
    private final String ddl = "CREATE TABLE IF NOT EXISTS bad_words (id BIGSERIAL, word VARCHAR, UNIQUE(word));";

    @Autowired
    public FileLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final File fileWithWord = new File("bad_words.txt");

    public int loadFileToDB(String fileName) {
        int countWords = 0;
        try (Scanner scanner = new Scanner(new File(fileName));
             Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            createTable();
            connection.setAutoCommit(false);
            while (scanner.hasNext()) {
                String word = scanner.nextLine().trim();
                if (word == null) {
                    continue;
                }
                statement.setString(1, word);
                statement.addBatch();
                countWords++;
            }
            try {
                statement.executeBatch();
                connection.commit();
            } catch (BatchUpdateException e) {
                LOGGER.error(e.getMessage());
                connection.rollback();
            }

            connection.setAutoCommit(true);
            LOGGER.info("Data load ({} records) - OK", countWords);
        } catch (FileNotFoundException | SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return countWords;
    }

    private void createTable() throws SQLException {
        DbUtil.applyDdl(ddl, dataSource);
    }
}
