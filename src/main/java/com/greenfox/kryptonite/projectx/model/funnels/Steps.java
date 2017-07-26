package com.greenfox.kryptonite.projectx.model.funnels;


import com.greenfox.kryptonite.projectx.model.pageviews.DataAttributes;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Steps {

  private Long id;
  private String type;
  private StepAttributes attributes;
}
