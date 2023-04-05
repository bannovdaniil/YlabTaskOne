package io.ylab.intensive.lesson04.movie;

import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.movie.util.HttpsDownloader;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

class MovieLoaderImplTest {
    private static DataSource dataSource;
    private static File testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = File.createTempFile("test-", ".txt");
        testFile.deleteOnExit();
    }

    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @BeforeAll
    static void beforeAll() throws SQLException {
        String createMovieTable = "drop table if exists movie;"
                + "CREATE TABLE IF NOT EXISTS movie (\n"
                + "\tid bigserial NOT NULL,\n"
                + "\t\"year\" int4,\n"
                + "\tlength int4,\n"
                + "\ttitle varchar,\n"
                + "\tsubject varchar,\n"
                + "\tactors varchar,\n"
                + "\tactress varchar,\n"
                + "\tdirector varchar,\n"
                + "\tpopularity int4,\n"
                + "\tawards bool,\n"
                + "\tCONSTRAINT movie_pkey PRIMARY KEY (id)\n"
                + ");";
        dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createMovieTable, dataSource);
    }

    @Test
    void downloadFile() throws IOException {
        new HttpsDownloader().downloadFile("https://ya.ru/", testFile, "UTF-8");
        StringBuilder sb = new StringBuilder();
        try (Scanner sc = new Scanner(testFile)) {
            while (sc.hasNext()) {
                sb.append(sc.nextLine());
            }
        }
        Assertions.assertTrue(sb.toString().contains("Yandex"));
    }

    @Test
    void loadMovie() throws FileNotFoundException {
        int expect = 3;
        String sql = "SELECT COUNT(*) as count FROM movie;";

        List<String> content = List.of(
                "Year;Length;Title;Subject;Actor;Actress;Director;Popularity;Awards;*Image",
                "INT;INT;STRING;CAT;CAT;CAT;CAT;INT;BOOL;STRING",
                "1990;111;Tie Me Up! Tie Me Down!;Comedy;Banderas, Antonio;Abril, Victoria;Almodóvar, Pedro;68;No;NicholasCage.png",
                "1991;113;High Heels;Comedy;Bosé, Miguel;Abril, Victoria;Almodóvar, Pedro;68;No;NicholasCage.png",
                "1983;104;Dead Zone, The;Horror;Walken, Christopher;Adams, Brooke;Cronenberg, David;79;No;NicholasCage.png"
        );

        try (PrintWriter pw = new PrintWriter(testFile)) {
            for (String line : content) {
                pw.println(line);
            }
            pw.flush();
        }

        new MovieLoaderImpl(dataSource).loadData(testFile);

        int result = 0;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                result = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expect, result);
    }
}