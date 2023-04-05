package io.ylab.intensive.lesson04.filesort;

import io.ylab.intensive.lesson04.DbUtil;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class FileSorterTest {
    private final static int COUNT = 1_000_000;

    public static void main(String[] args) throws SQLException, IOException {
        DataSource dataSource = initDb();
        File data = new File("data.txt");

        long hashCode = new GeneratorWithHash().generate(data, COUNT);
        FileSorter fileSorterBatch = new FileSortImpl(dataSource);
        FileSorter fileSorterSerial = new FileSortImplSerial(dataSource);

        System.out.print("Batch append: ");
        testSort(data, hashCode, fileSorterBatch);

        System.out.println("Clear DataBase.");
        clearData(dataSource);
        System.out.print("Serial append: ");
        testSort(data, hashCode, fileSorterSerial);
    }

    private static void testSort(File data, long hashCode, FileSorter fileSorter) {
        long timeStart = System.currentTimeMillis();
        File res = fileSorter.sort(data);
        System.out.println("Time: " + (System.currentTimeMillis() - timeStart) / 1000.0);
        System.out.print("Check file: ");
        System.out.println(new ValidatorWithSizeAndHash(res, COUNT, hashCode).isSorted());
    }

    private static void clearData(DataSource dataSource) throws SQLException {
        final String sql = "TRUNCATE TABLE numbers;";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public static DataSource initDb() throws SQLException {
        String createSortTable = ""
                + "drop table if exists numbers;"
                + "CREATE TABLE if not exists numbers (\n"
                + "\tval bigint\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createSortTable, dataSource);
        return dataSource;
    }
}
