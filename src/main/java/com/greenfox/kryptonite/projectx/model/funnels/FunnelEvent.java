package com.greenfox.kryptonite.projectx.model.funnels;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FunnelEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String path;
  private int count;
  @ManyToOne
  @JoinColumn (name = "id_funnel")
  private Funnel funnel;

  public FunnelEvent(String path, int count, Funnel funnel) {
    this.path = path;
    this.count = count;
    this.funnel = funnel;
  }
}
