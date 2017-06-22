package com.greenfox.kryptonite.projectx.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestTemplateService {

  private RestTemplate restTemplate = new RestTemplate();

}
