package com.greenfox.kryptonite.projectx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class HotelMonitoringApplication {

  public static void main(String[] args) {
    SpringApplication.run(HotelMonitoringApplication.class, args);
    System.out.println("Kryptonite");
  }
}

