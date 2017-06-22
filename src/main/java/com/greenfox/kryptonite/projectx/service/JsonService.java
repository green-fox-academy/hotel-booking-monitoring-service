package com.greenfox.kryptonite.projectx.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.Service;
import com.greenfox.kryptonite.projectx.model.Services;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JsonService {

  private static final String ERROR = "SYNTAX ERROR: ";

  public Services readFiles(String datapath) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<String> rawLines = new ArrayList<>();
    Path myPath = Paths.get(datapath);

    try {
      rawLines = Files.readAllLines(myPath);
    } catch (IOException ex) {
      System.out.println(ERROR + "READ");
      writeToFile(datapath);
    }

    StringBuilder fileContentConvertedToJson = new StringBuilder();
    for (String rawLine : rawLines) {
      fileContentConvertedToJson.append(rawLine);
    }
    return mapper.readValue(fileContentConvertedToJson.toString(), Services.class);
  }

  public boolean writeToFile(String datapath)
      throws JsonProcessingException {
    boolean runTimeChecker = true;

    Path myPath = Paths.get(datapath);
    List<String> dataString = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    dataString.add(mapper.writeValueAsString(addContactsToWrite()));

    try {
      Files.write(myPath, dataString);
    } catch (IOException ex) {
      System.out.println(ERROR + "WRITE");
      runTimeChecker = false;
    }
    return runTimeChecker;
  }

  public static Services addContactsToWrite() {
    Service resize = new Service("https://hotel-booking-resize-service.herokuapp.com", "berta@greenfox.com");
    Service notification = new Service("https://booking-notification-service.herokuapp.com", "tojasmamusza@greenfox.com");
    Service userSerice = new Service("https://hotel-booking-user-service.herokuapp.com", "imi@greenfox.com");
    Service payment = new Service("https://hotel-booking-payment.herokuapp.com", "yesyo@greenfox.com");
    Service resource = new Service("https://booking-resource.herokuapp.com", "MrPoopyButthole@podi.com");
    List<Service> serviceList = new ArrayList<>(
        Arrays.asList(resize, notification, userSerice, payment, resource));
    return new Services(serviceList);
  }
}
