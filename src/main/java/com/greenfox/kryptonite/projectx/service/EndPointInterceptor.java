package com.greenfox.kryptonite.projectx.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class EndPointInterceptor extends HandlerInterceptorAdapter {


  private ProjectXService projectXService = new ProjectXService();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    projectXService.endpointLogger(request.getPathInfo());
    return true;
  }
}
