package com.greenfox.kryptonite.projectx.service;


import com.greenfox.kryptonite.projectx.model.Message;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonService {

  private static final String DATAPATH = "monitoring-services.json";
  private static final String ERROR = "SYNTAX ERROR: ";

  static List<Message> readFiles() {
    List<String> rawLines = new ArrayList<>();
    Path myPath = Paths.get(DATAPATH);
    try {
      rawLines = Files.readAllLines(myPath);
    } catch (IOException ex) {
      System.out.println(ERROR + "READ");
    }
    List<Message> services = new ArrayList<>();
    for (String rawLine : rawLines) {
      String[] tempElements = rawLine.split(";");
      Message message = new Message();
      todos.add(message);
    }
    return todos;
  }

  static void writeToFile(List<Message> data) {
    Path myPath = Paths.get(DATAPATH);
    List<String> dataString = new ArrayList<>();
    try {
      Files.write(myPath, dataString);
    } catch (IOException ex) {
      System.out.println(ERROR + "WRITE");
    }
  }
}
