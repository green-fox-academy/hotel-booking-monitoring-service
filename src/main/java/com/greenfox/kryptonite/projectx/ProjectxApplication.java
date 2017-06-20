package com.greenfox.kryptonite.projectx;

import org.apache.logging.log4j.core.lookup.MainMapLookup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectxApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProjectxApplication.class, args);
    System.out.println("Kryptonite");
  }
}

