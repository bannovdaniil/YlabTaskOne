package io.ylab.intensive.lesson03.org_structure;

import com.sun.jdi.InvalidTypeException;
import io.ylab.intensive.lesson03.org_structure.model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

class OrgStructureParserImplTests {
    private final String separator = File.separator;
    private final String scvFilePatch = "src" + separator + "test" + separator + "resources" + separator;


    @ParameterizedTest
    @DisplayName("Parse CSV file")
    @CsvSource({
            "employees.csv, 1, 3",
            "employees_boss_not_first.csv, 1, 4"
    })
    void parseStructure(String fileName, long expectedBossId, int expectedCountSubordinate) throws IOException, InvalidTypeException {
        File fileCsv = new File(scvFilePatch + fileName);
        OrgStructureParser orgStructureParser = new OrgStructureParserImpl();

        Employee boss = orgStructureParser.parseStructure(fileCsv);

        Assertions.assertEquals(expectedBossId, boss.getId());
        Assertions.assertEquals(expectedCountSubordinate, boss.getSubordinate().size());
    }

    @Test
    @DisplayName("Bad file")
    void badTypeOfFile() {
        String expectedMessage = "Неверный тип файла или переданный файл не содержит необходимые поля.";
        final File fileCsv = new File(scvFilePatch + "bad_header.csv");
        OrgStructureParser orgStructureParser = new OrgStructureParserImpl();

        InvalidTypeException ex = Assertions.assertThrows(InvalidTypeException.class,
                () -> orgStructureParser.parseStructure(fileCsv)
        );

        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    @DisplayName("Boss not Found Exceptions")
    void bossNotFound() {
        String expectedMessage = "Boss not found.";
        final File fileCsv = new File(scvFilePatch + "employees_without_boss.csv");
        OrgStructureParser orgStructureParser = new OrgStructureParserImpl();

        NoSuchElementException ex = Assertions.assertThrows(NoSuchElementException.class,
                () -> orgStructureParser.parseStructure(fileCsv)
        );

        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }
}