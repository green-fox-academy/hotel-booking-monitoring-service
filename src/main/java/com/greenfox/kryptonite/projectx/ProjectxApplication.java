package com.greenfox.kryptonite.projectx;

import com.greenfox.kryptonite.projectx.model.Send;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectxApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProjectxApplication.class, args);
    System.out.println("Kryptonite");
  }
}

