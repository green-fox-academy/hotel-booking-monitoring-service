package com.greenfox.kryptonite.projectx.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter
@Getter
@NoArgsConstructor
public class Timestamp {

  public String getDate() {
    return new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date());
  }
}
