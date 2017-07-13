package com.greenfox.kryptonite.projectx.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelService;
import com.greenfox.kryptonite.projectx.model.hotelservices.HotelServices;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IOService {

  private static final String ERROR = "SYNTAX ERROR: ";

  public HotelServices readFiles(String datapath) throws IOException {
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
    return mapper.readValue(fileContentConvertedToJson.toString(), HotelServices.class);
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

  public static HotelServices addContactsToWrite() {
    HotelService resize = new HotelService("https://hotel-booking-resize-service.herokuapp.com", "berta@greenfox.com");
    HotelService notification = new HotelService("https://booking-notification-service.herokuapp.com", "tojasmamusza@greenfox.com");
    HotelService userSerice = new HotelService("https://hotel-booking-user-service.herokuapp.com", "imi@greenfox.com");
    HotelService payment = new HotelService("https://hotel-booking-payment.herokuapp.com", "yesyo@greenfox.com");
    HotelService resource = new HotelService("https://booking-resource.herokuapp.com", "MrPoopyButthole@podi.com");
    List<HotelService> hotelServiceList = new ArrayList<>(
        Arrays.asList(resize, notification, userSerice, payment, resource));
    return new HotelServices(hotelServiceList);
  }
}
