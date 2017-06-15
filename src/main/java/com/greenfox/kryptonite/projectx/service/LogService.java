package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.NoArgsConstructor;


public class LogService {

  public String logLevel ="INFO";

  public LogService() {
    this.logLevel = "INFO";
  }

  public int log(String type, String message) {
    Log log = createLog(type,message);
    if (checkLogLevel(log)) {
      selectPrintln(log);
    }
    return log.getReturnValue();
  }

  private Log createLog(String type, String message) {
    if (type.equals("DEBUG")){
      return new Log(type,message,500);
    } else if (type.equals("INFO")) {
      return new Log(type,message,400);
    } else if (type.equals("WARN")) {
      return new Log(type,message,300);
    } else if (type.equals("ERROR")) {
      return new Log(type,message,200);
    } else {
      return new Log();
    }
  }

  private void selectPrintln(Log log) {
    if (log.getType().equals("DEBUG") || log.getType().equals("INFO")) {
      System.out.println(log);
    } else if (log.getType().equals("WARN") || log.getType().equals("ERROR")) {
      System.err.println(log);
    }
  }

  public boolean checkLogLevel(Log log) {
    try {
      logLevel = System.getenv("LOGLEVEL");
      System.out.println(System.getenv("LOGLEVEL"));
      System.out.println(logLevel);
    } catch (Exception ex) {
      System.err.println("Log level is not set. Logging on default level: INFO");
    }

    if (logLevel.equals("DEBUG")) {
      return true;
    } else if (logLevel.equals("WARN")) {
      return (log.getReturnValue() <= 300);
    } else if (logLevel.equals("ERROR")) {
      return (log.getReturnValue() == 200);
    } else {
      return (log.getReturnValue() <= 400);
    }

  }
}
