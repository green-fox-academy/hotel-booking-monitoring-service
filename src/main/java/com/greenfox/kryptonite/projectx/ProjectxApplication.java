package com.greenfox.kryptonite.projectx;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectxApplication {

  public static void main(String[] args) {

    SpringApplication.run(ProjectxApplication.class, args);

    Flyway flyway = new Flyway();
    flyway.setDataSource(System.getenv("JDBC_DATABASE_URL"),
        System.getenv("JDBC_DATABASE_USERNAME"),
        System.getenv("JDBC_DATABASE_PASSWORD"));
    flyway.migrate();
  }
}

