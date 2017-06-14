package com.greenfox.kryptonite.projectx.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogService {

  public int debug(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    System.out.println("DEBUG " + date + " greenfox-kryptonite.herokuapp.com " + message);
    return 500;
  }

  public int info(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    System.out.println ("INFO " + date + " greenfox-kryptonite.herokuapp.com " + message);
    return 400;
  }

  public int warn(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    System.err.println ("WARN " + date + " greenfox-kryptonite.herokuapp.com " + message);
    return 300;
  }

  public int error(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    System.err.println ("ERROR " + date + " greenfox-kryptonite.herokuapp.com " + message);
    return 200;
  }
}
