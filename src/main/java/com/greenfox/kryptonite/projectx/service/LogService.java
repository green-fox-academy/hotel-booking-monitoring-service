package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogService {

  public int log(String type, String message) {
    Log log = createLog(type,message);
    selectPrintln(log);
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


//  public int debug(String message) {
//    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
//    System.out.println("DEBUG " + date + " greenfox-kryptonite.herokuapp.com " + message);
//    return 500;
//  }
//
//  public int info(String message) {
//    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
//    System.out.println ("INFO " + date + " greenfox-kryptonite.herokuapp.com " + message);
//    return 400;
//  }
//
//  public int warn(String message) {
//    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
//    System.err.println ("WARN " + date + " greenfox-kryptonite.herokuapp.com " + message);
//    return 300;
//  }
//
//  public int error(String message) {
//    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
//    System.err.println ("ERROR " + date + " greenfox-kryptonite.herokuapp.com " + message);
//    return 200;
//  }
}
