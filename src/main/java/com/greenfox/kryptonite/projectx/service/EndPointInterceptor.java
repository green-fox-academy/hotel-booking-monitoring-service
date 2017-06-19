package com.greenfox.kryptonite.projectx.service;

import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class EndPointInterceptor extends HandlerInterceptorAdapter {

  private ProjectXService projectXService = new ProjectXService();


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws NoHandlerFoundException {
      projectXService.endpointLogger(request.getRequestURI());
      return true;
  }
}
