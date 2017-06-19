package com.greenfox.kryptonite.projectx.configuration;


import com.greenfox.kryptonite.projectx.service.EndPointInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class EndPointLoggerConfig extends WebMvcConfigurerAdapter {

  private EndPointInterceptor endPointInterceptor;

  public EndPointLoggerConfig() {
    this.endPointInterceptor = new EndPointInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(endPointInterceptor)
            .addPathPatterns("/*");
  }
}
