package com.greenfox.kryptonite.projectx.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogService {

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
    System.out.println ("INFO " + date + " greenfox-kryptonite.herokuapp.com " + message);
    printStars();
    return 400;
  }

  public int warn(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    printStars();
    System.err.println ("WARN " + date + " greenfox-kryptonite.herokuapp.com " + message);
    printStars();
    return 300;
  }

  public int error(String message) {
    String date = new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
    printStars();
    System.err.println ("ERROR " + date + " greenfox-kryptonite.herokuapp.com " + message);
    printStars();
    return 200;
  }

  public void printStars(){
    for(int i = 0;i < 4; ++i){
      System.out.println("*");
    }
  }
}
