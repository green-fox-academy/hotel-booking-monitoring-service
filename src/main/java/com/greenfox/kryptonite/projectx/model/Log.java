package com.greenfox.kryptonite.projectx.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Log {

  Timestamp time = new Timestamp();

  private String type;
  private String date;
  private final String appName = "greenfox-kryptonite.herokuapp.com";
  private String message;
  private int returnValue;

  private Map<String, Integer> levels = new HashMap<String, Integer>() {{
    put("DEBUG", 500);
    put("INFO", 400);
    put("WARN", 300);
    put("ERROR", 200);
  }};

  public Log(String type, String message) {
    this.date = time.getDate();
    this.type = type;
    this.message = message;
    this.returnValue = levels.get(type);
  }

  public Log(String type, String date, String message, int returnValue) {
    this.type = type;
    this.date = date;
    this.message = message;
    this.returnValue = returnValue;
  }

  @Override
  public String toString() {
    return type +
        " " + date +
        " " + appName +
        " " + message;
  }
}
