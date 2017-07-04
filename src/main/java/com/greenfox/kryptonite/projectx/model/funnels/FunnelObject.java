package com.greenfox.kryptonite.projectx.model.funnels;

import com.greenfox.kryptonite.projectx.model.pageviews.EventToDatabase;
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
  private List<EventToDatabase> included;

  public FunnelObject(List<EventToDatabase> included) {
    this.included = included;
  }
}
