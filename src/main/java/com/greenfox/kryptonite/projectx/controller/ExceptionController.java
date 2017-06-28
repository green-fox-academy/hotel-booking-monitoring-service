package com.greenfox.kryptonite.projectx.controller;

import com.greenfox.kryptonite.projectx.model.ErrorMessage;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

  private Logger logger = LogManager.getLogger(MainRestController.class);

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(Exception.class)
  public String somethingWentWrong(Exception ex, HttpServletRequest request) {
    logger.error("Something went wrong");
    return "error";
  }

}
