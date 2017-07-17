package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FunnelData {

  private String type;
  private Long id;
  private Relationships relationships;
  private List<Steps> included;

  public FunnelData(Long id, Relationships relationships, List<Steps> included) {
    this.id = id;
    this.type = "funnels";
    this.relationships = relationships;
    this.included = included;
  }
}
