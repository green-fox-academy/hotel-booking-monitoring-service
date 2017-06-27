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

  @Autowired
  MonitoringService monitoringService;

  @Before("execution(* com.greenfox.kryptonite.projectx.controller.MainRestController+.*(..))")
  public void LoggingAdvice() {
    logger.info("Hello");
  }

  @Before("databasecheck()")
  public void LoggingAdvice2() {
    logger.info("Checking database");
  }

  @After("databasecheck()")
  public void LoggingAdvice3() {
    logger.info("Database checked");
  }

  @AfterReturning(pointcut = "databasecheck()", returning = "retVal")
  public void doAfterReturningTask(Object retVal) throws Exception {
    System.out.println(retVal.toString());
    if (retVal == new BookingStatus("ok", "ok", monitoringService.queueCheck())) {
      logger.info("Status ok");
    } else if (retVal == new BookingStatus("ok", "error", monitoringService.queueCheck())) {
      logger.error("Status error");
    }
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
