package com.greenfox.kryptonite.projectx.repository;

import com.greenfox.kryptonite.projectx.model.funnels.Steps;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends CrudRepository<Steps, Long> {
}
