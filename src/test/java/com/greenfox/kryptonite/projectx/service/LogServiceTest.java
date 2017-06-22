package com.greenfox.kryptonite.projectx.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LogServiceTest {
  private Logger logger = LogManager.getLogger(this.getClass());

  @Test
  public void testAllLogs() {
    logger.debug("This is a debug message");
    logger.info("This is an info message");
    logger.warn("This is a warn message");
    logger.error("This is an error message");
  }
}