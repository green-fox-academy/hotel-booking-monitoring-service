package com.greenfox.kryptonite.projectx.service;

import com.greenfox.kryptonite.projectx.model.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogServiceTest {

  private LogService logging;
  private Logger logger = LogManager.getLogger(this.getClass());


  @Before
  public void setup() throws Exception {
    this.logging = new LogService();
  }

  @Test
  public void testDebugLogging() {
    logger.debug("This is a debug message");
    assertEquals(logging.log("DEBUG","message").getReturnValue(), 500);
  }

  @Test
  public void testInfoLogging() {
    logger.info("This is an info message");
    assertEquals(logging.log("INFO","message").getReturnValue(), 400);
  }

  @Test
  public void testWarnLogging() {
    logger.warn("This is a warn message");
    assertEquals(logging.log("WARN","message").getReturnValue(), 300);
  }

  @Test
  public void testErrorLogging() {
    logger.error("This is an error message");
    assertEquals(logging.log("ERROR","message").getReturnValue(), 200);
  }

}