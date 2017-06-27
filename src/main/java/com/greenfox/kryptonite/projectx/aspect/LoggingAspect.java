package com.greenfox.kryptonite.projectx.aspect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Around;
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
  private Logger logger = LogManager.getLogger(this.getClass());

  @Before("execution(* com.greenfox.kryptonite.projectx.controller.MainRestController+.*(..))")
  public void LoggingAdvice() {
    logger.info("Hello");
  }

}
