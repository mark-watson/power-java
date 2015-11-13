package com.markwatson.km;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by markw on 9/20/15.
 */
public class ExcelData {
  public List<List<List<String>>> sheetsAndRows = new ArrayList<>();
  private List<List<String>> currentSheet = null;

  public ExcelData(String contents) {
    Pattern.compile("\n")
        .splitAsStream(contents)
        .forEach((String line) -> handleContentsStream(line));
    if (currentSheet != null) sheetsAndRows.add(currentSheet); // to pick up last section's rows
  }

  private void handleContentsStream(String nextLine) {
    if (nextLine.startsWith("Sheet")) {
      if (currentSheet != null) sheetsAndRows.add(currentSheet);
      currentSheet = new ArrayList<>();
    } else {
      String[] columns = nextLine.substring(1).split("\t");
      currentSheet.add(Arrays.asList(columns));
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("<ExcelData # sheets: " + sheetsAndRows.size() + "\n");
    for (List<List<String>> sheet : sheetsAndRows) {
      sb.append("  <Sheet\n");
      for (List<String> row : sheet) {
        sb.append("    <row " + row + ">\n");
      }
      sb.append("  >\n>\n");
    }
    return sb.toString();
  }
}
