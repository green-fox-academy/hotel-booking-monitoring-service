package com.greenfox.kryptonite.projectx.aspect;

import com.greenfox.kryptonite.projectx.controller.MainRestController;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Getter
@Setter
@NoArgsConstructor
public class LoggingAspect {
  private Logger logger = LogManager.getLogger(MainRestController.class);

  @Pointcut("execution(* com.greenfox.kryptonite.projectx.service.MonitoringService.databaseCheck(*)) ")
  public void databasecheck(){}

  @Before("databasecheck()")
  public void LoggingAdvice2() {
    logger.info("Checking database");
  }

  @After("databasecheck()")
  public void LoggingAdvice3() {
    logger.info("Database checked");
  }

  @Pointcut("execution(* com.greenfox.kryptonite.projectx.controller.MainRestController..*(..))")
  public void controller() {
  }

  @Before("controller() && args(.., request)")
  public void logBefore(JoinPoint joinPoint, HttpServletRequest request) {
    logger.info("HTTP-REQUEST=" + request.getMethod() + " " + request.getRequestURI());
  }
}
