package com.greenfox.kryptonite.projectx.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {

  String message;
  String hostname;
  String date;

  public Message(String message, String hostname) {

    Date newDate = new Date();
    this.date = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(newDate);
    this.message = message;
    this.hostname = hostname;
  }
}
