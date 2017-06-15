package com.greenfox.kryptonite.projectx.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Log {

  private String type;
  private String date;
  private final String appName = "greenfox-kryptonite.herokuapp.com";
  private String message;
  private int returnValue;

  public Log(String type, String message, int returnValue) {
    this.date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    this.type = type;
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
