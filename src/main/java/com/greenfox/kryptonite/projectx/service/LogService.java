package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Log;

public class LogService {

<<<<<<< HEAD
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
    if (type.equals("DEBUG")) {
      return new Log(type, message, 500);
    } else if (type.equals("INFO")) {
      return new Log(type, message, 400);
    } else if (type.equals("WARN")) {
      return new Log(type, message, 300);
    } else if (type.equals("ERROR")) {
      return new Log(type, message, 200);
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

  private void checkLogLevel() {
    logLevel = System.getenv("LOGLEVEL");
    if (logLevel == null) {
      System.err.println("Log level is not set. Logging on default level: INFO");
      logLevel = "INFO";
    }
  }

  private boolean isLogLevelOk(Log log) {
    if (logLevel.equals("DEBUG")) {
      return true;
    } else if (logLevel.equals("WARN")) {
      return (log.getReturnValue() <= 300);
    } else if (logLevel.equals("ERROR")) {
      return (log.getReturnValue() == 200);
    } else {
      return (log.getReturnValue() <= 400);
    }
=======
  public int debug(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    printStars();
    System.out.println("DEBUG " + date + " greenfox-kryptonite.herokuapp.com " + message);
    printStars();
    return 500;
  }

  public int info(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    printStars();
    System.out.println("INFO " + date + " greenfox-kryptonite.herokuapp.com " + message);
    printStars();
    return 400;
  }

  public int warn(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    printStars();
    System.err.println("WARN " + date + " greenfox-kryptonite.herokuapp.com " + message);
    printStars();
    return 300;
  }

  public int error(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    printStars();
    System.err.println("ERROR " + date + " greenfox-kryptonite.herokuapp.com " + message);
    printStars();
    return 200;
>>>>>>> f685d4e0ad2e7f3b70eac1bd3b614dfebaf3288f
  }

  public void printStars() {
    for (int i = 0; i < 2; ++i) {
      System.out.println("*");
    }
  }
}
