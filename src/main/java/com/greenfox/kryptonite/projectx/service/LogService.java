package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Log;
import java.util.Map;

public class LogService {

  private String logLevel;

  public LogService() {
    this.logLevel = "INFO";
  }

  public Log log(String type, String message) {
    Log log = createLog(type,message);
    checkLogLevel();
    if (isLogLevelOk(log)) {
      selectPrintln(log);
    }
    return log;
  }

  private Log createLog(String type, String message) {
    return new Log(type, message);
  }

  private void selectPrintln(Log log) {
    if (log.getType().equals("DEBUG") || log.getType().equals("INFO")) {
      System.out.println(log);
    } else if (log.getType().equals("WARN") || log.getType().equals("ERROR")) {
      System.err.println(log);
    }
  }

  private void checkLogLevel() {
    logLevel = System.getenv("LOGLEVEL");
    if (logLevel == null) {
      System.err.println("Log level is not set. Logging on default level: INFO");
      logLevel = "INFO";
    }
  }

  private boolean isLogLevelOk(Log log) {
    boolean isOk = (log.getReturnValue() <= log.getLevels().get("INFO"));
    for (Map.Entry<String,Integer> level : log.getLevels().entrySet()) {
      if (logLevel.equals(level.getKey())) {
        isOk = (log.getReturnValue() <= level.getValue());
      }
    }
    return isOk;

//    if (logLevel.equals("DEBUG")) {
//      return true;
//    } else if (logLevel.equals("WARN")) {
//      return (log.getReturnValue() <= 300);
//    } else if (logLevel.equals("ERROR")) {
//      return (log.getReturnValue() == 200);
//    } else {
//      return (log.getReturnValue() <= 400);
//    }
  }
}
