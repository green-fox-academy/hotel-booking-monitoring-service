package com.greenfox.kryptonite.projectx.aspect;

import com.greenfox.kryptonite.projectx.controller.MainRestController;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Aspect
@Component
@Getter
@Setter
@NoArgsConstructor
public class LoggingAspect {
  private Logger logger = LogManager.getLogger(MainRestController.class);

  @Before("databasecheck()")
  public void LoggingAdvice2() {
    logger.info("Checking database");
  }

  @After("databasecheck()")
  public void LoggingAdvice3() {
    logger.info("Database checked");
  }


  @Pointcut("execution(* com.greenfox.kryptonite.projectx.service.MonitoringService.databaseCheck(*)) ")
  public void databasecheck(){}

  @Pointcut("execution(* com.greenfox.kryptonite.projectx.controller.MainRestController..*(..))")
  public void controller() {
  }

  @Before("controller() && args(.., request)")
  public void logBefore(JoinPoint joinPoint, HttpServletRequest request) {
    logger.info("HTTP-REQUEST=" + "" + request.getMethod() + " " + request.getRequestURI());
    System.out.println(joinPoint.toString());
  }
}
