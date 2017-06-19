package com.greenfox.kryptonite.projectx.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
public class Log {

  @Autowired
  Timestamp time;

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

  @Override
  public String toString() {
    return type +
        " " + date +
        " " + appName +
        " " + message;
  }
}
