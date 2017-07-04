package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataObject {

  private Long id;
  private String type;
  private Relationships relationships;
  private List<Steps> included;
}
