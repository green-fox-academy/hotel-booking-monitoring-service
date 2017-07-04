package com.greenfox.kryptonite.projectx.model.funnels;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class FunnelObject {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @OneToMany
  private List<Steps> included;

  public FunnelObject(List<Steps> included) {
    this.included = included;
  }
}
