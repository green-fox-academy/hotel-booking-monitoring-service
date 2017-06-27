package com.greenfox.kryptonite.projectx.aspect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Getter
@Setter
@NoArgsConstructor
public class LoggingAspect {
  private Logger logger = LogManager.getLogger(this.getClass());

  @Before("execution(* com.greenfox.kryptonite.projectx.controller.MainRestController+.*(..))")
  public void LoggingAdvice() {
    logger.info("Hello");
  }

  @Before("execution(public com.greenfox.kryptonite.projectx..model.BookingStatus com.greenfox.kryptonite.projectx.service.MonitoringService .databaseCheck(com.greenfox.kryptonite.projectx.repository.HeartbeatRepository))")
  public void LoggingAdvice2() {
    logger.info("Database checked");
  }

  

}
