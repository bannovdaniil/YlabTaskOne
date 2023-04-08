package io.ylab.intensive.lesson03.org_structure;

import com.sun.jdi.InvalidTypeException;
import io.ylab.intensive.lesson03.org_structure.model.Employee;

import java.io.File;
import java.io.IOException;

public class OrgStructureParserImplTest {
    public static void main(String[] args) throws IOException, InvalidTypeException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File fileCsv = new File(classLoader.getResource("employees.csv").getFile());

        OrgStructureParser orgStructureParser = new OrgStructureParserImpl();

        Employee boss = orgStructureParser.parseStructure(fileCsv);

        System.out.println(boss);
        getSubordinates(boss, 1);
    }

    private static void getSubordinates(Employee boss, int level) {
        System.out.println("  ".repeat(level) + ">Subordinate for: " + boss.getName());
        for (var subEmployee : boss.getSubordinate()) {
            System.out.println("  ".repeat(level) + subEmployee);
            if (subEmployee.getSubordinate().size() > 0) {
                getSubordinates(subEmployee, level + 2);
            }
        }
    }
}
