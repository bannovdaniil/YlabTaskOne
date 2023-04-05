package io.ylab.intensive.lesson04.filesort;

import io.ylab.intensive.lesson04.DbUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class FileSortImplTest {
    private static DataSource dataSource;
    private static File testFile;

    @BeforeEach
    void setUp() throws IOException, SQLException {
        String createSortTable = ""
                + "drop table if exists numbers;"
                + "CREATE TABLE if not exists numbers (\n"
                + "\tval bigint\n"
                + ");";
        dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createSortTable, dataSource);

        testFile = File.createTempFile("test-", ".txt");
        testFile.deleteOnExit();
    }

    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void sortBatch() throws FileNotFoundException {
        FileSorter fileSorterBatch = new FileSortImpl(dataSource);

        sortHelper(fileSorterBatch, "data_out.txt");
    }

    @Test
    void sortSerial() throws FileNotFoundException {
        FileSorter fileSorterSerial = new FileSortImplSerial(dataSource);

        sortHelper(fileSorterSerial, "data_out_serial.txt");
    }

    private void sortHelper(FileSorter fileSorter, String fileOutName) throws FileNotFoundException {
        List<Long> content = List.of(
                1120564433373397448L,
                427743039620907418L,
                -4332769499345923923L,
                -4826306333514128048L,
                6380685377058609312L
        );

        List<Long> expect = new ArrayList<>(content);
        expect.sort(Collections.reverseOrder());


        try (PrintWriter pw = new PrintWriter(testFile)) {
            for (Long num : content) {
                pw.println(num);
            }
            pw.flush();
        }

        fileSorter.sort(testFile);

        List<Long> result = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(fileOutName))) {
            while (sc.hasNext()) {
                result.add(sc.nextLong());
            }
        }

        Assertions.assertEquals(expect, result);
    }
}