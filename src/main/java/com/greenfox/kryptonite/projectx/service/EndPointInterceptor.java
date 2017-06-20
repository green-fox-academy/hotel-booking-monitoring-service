package com.greenfox.kryptonite.projectx.service;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class EndPointInterceptor extends HandlerInterceptorAdapter {

  private MonitoringService monitoringService = new MonitoringService();


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      monitoringService.endpointLogger(request.getRequestURI());
      return true;
  }

}
