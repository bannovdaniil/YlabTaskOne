package io.ylab.intensive.lesson03.org_structure;

import com.sun.jdi.InvalidTypeException;
import io.ylab.intensive.lesson03.org_structure.model.Employee;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class OrgStructureParserImpl implements OrgStructureParser {
  private final static String SCV_HEADER = "id;boss_id;name;position";

  @Override
  public Employee parseStructure(File csvFile) throws IOException, InvalidTypeException {
    Optional<Employee> boss = Optional.empty();
    Map<Long, Employee> employeeList = new HashMap<>();

    try (Scanner scanner = new Scanner(csvFile)) {
      String headerLine = scanner.nextLine().trim();
      if (!isCorrectCsvFile(headerLine)) {
        throw new InvalidTypeException("Неверный тип файла или переданный файл не содержит необходимые поля.");
      }

      while (scanner.hasNext()) {
        String line = scanner.nextLine().trim();

        Employee employee = employeeMapperFromString(line);
        employeeList.put(employee.getId(), employee);
      }
    }

    for (var employee : employeeList.values()) {
      if (employee.getBossId() != null) {
        Employee subBoss = employeeList.get(employee.getBossId());
        employee.setBoss(subBoss);
        subBoss.getSubordinate().add(employee);
      } else {
        boss = Optional.of(employee);
      }
    }

    return boss.orElseThrow(() -> new NoSuchElementException("Boss not found."));
  }

  private Employee employeeMapperFromString(String line) {
    String[] splitData = line.split(";");

    Long id = Long.parseLong(splitData[0]);
    Long bossId = splitData[1].isEmpty() ? null : Long.parseLong(splitData[1]);
    String name = splitData[2];
    String position = splitData[3];

    Employee employee = new Employee();
    employee.setId(id);
    employee.setBossId(bossId);
    employee.setName(name);
    employee.setPosition(position);

    return employee;
  }

  private boolean isCorrectCsvFile(String line) {
    return SCV_HEADER.equalsIgnoreCase(line);
  }
}
