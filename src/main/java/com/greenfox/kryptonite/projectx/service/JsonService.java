package com.greenfox.kryptonite.projectx.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.Message;
import com.greenfox.kryptonite.projectx.model.Service;
import com.greenfox.kryptonite.projectx.model.Services;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonService {

  private static final String DATAPATH = "monitoring-services.json";
  private static final String ERROR = "SYNTAX ERROR: ";

  static Services readFiles() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<String> rawLines = new ArrayList<>();
    Path myPath = Paths.get(DATAPATH);

    try {
      rawLines = Files.readAllLines(myPath);
    } catch (IOException ex) {
      System.out.println(ERROR + "READ");
    }

    StringBuilder fileContentConvertedToJson = new StringBuilder();
    for (String rawLine : rawLines) {
      fileContentConvertedToJson.append(rawLine);
    }
    return mapper.readValue(fileContentConvertedToJson.toString(), Services.class);
  }

  static void writeToFile(Services servicesJson) throws JsonProcessingException {
    Path myPath = Paths.get(DATAPATH);
    List<String> dataString = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    dataString.add(mapper.writeValueAsString(servicesJson));
    
    try {
      Files.write(myPath, dataString);
    } catch (IOException ex) {
      System.out.println(ERROR + "WRITE");
    }
  }
}
