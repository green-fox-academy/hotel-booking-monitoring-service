package com.greenfox.kryptonite.projectx.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;


@Component
public class Timestamp {

  public String getDate() {
    return new SimpleDateFormat("yyyy-MM-dd'T'KK:mm:ss'Z'").format(new Date());
  }
}
