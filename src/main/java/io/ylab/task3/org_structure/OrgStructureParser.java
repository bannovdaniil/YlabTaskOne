package io.ylab.task3.org_structure;

import com.sun.jdi.InvalidTypeException;
import io.ylab.task3.org_structure.model.Employee;

import java.io.File;
import java.io.IOException;

public interface OrgStructureParser {
  Employee parseStructure(File csvFile) throws IOException, InvalidTypeException;
}
