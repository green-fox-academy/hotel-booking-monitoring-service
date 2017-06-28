package com.greenfox.kryptonite.projectx.aspect;

import com.greenfox.kryptonite.projectx.controller.MainRestController;
import com.greenfox.kryptonite.projectx.model.BookingStatus;
import com.greenfox.kryptonite.projectx.repository.HeartbeatRepository;
import com.greenfox.kryptonite.projectx.service.MonitoringService;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Getter
@Setter
@NoArgsConstructor
public class LoggingAspect {
  private Logger logger = LogManager.getLogger(MainRestController.class);

  @Autowired
  MonitoringService monitoringService;

  @Autowired
  HeartbeatRepository heartbeatRepository;

  @Pointcut("execution(* com.greenfox.kryptonite.projectx.service.MonitoringService.databaseCheck(*)) ")
  public void databasecheck(){}

  @Before("databasecheck()")
  public void beforeDatabaseCheck() {
    logger.info("Checking database");
  }

  @After("databasecheck()")
  public void afterDatabaseCheck() {
    logger.info("Database checked");
  }

  @AfterReturning(pointcut = "databasecheck()", returning = "retVal")
  public void doAfterReturningTask(Object retVal) throws Exception {
    if (retVal == new BookingStatus("ok", "error", monitoringService.queueCheck()) &&
        heartbeatRepository == null) {
      logger.error("Database not present.");
      logger.debug("Database may not exist. Check database connection or existence.");
    } else if (retVal == new BookingStatus("ok", "ok", monitoringService.queueCheck()) ) {
      logger.info(
          "Database connection is ok and contains " + heartbeatRepository.count() + " element(s).");
    } else {
      logger.info("Database connection is ok.");
      logger.warn("Database is empty.");
    }
  }

  @Pointcut("execution(* com.greenfox.kryptonite.projectx.controller.MainRestController..*(..))")
  public void controller() {
  }

  @Before("controller() && args(.., request)")
  public void endpointLogging(HttpServletRequest request) {
    logger.info("HTTP-REQUEST=" + request.getMethod() + " " + request.getRequestURI());
  }
}
