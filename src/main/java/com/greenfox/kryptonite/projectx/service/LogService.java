package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogService {

  public int log(String type, String message) {
    Log log = createLog(type, message);
    selectPrintln(log);
    return log.getReturnValue();
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

}
