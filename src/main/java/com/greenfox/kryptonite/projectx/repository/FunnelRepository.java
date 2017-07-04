package com.greenfox.kryptonite.projectx.repository;

import com.greenfox.kryptonite.projectx.model.funnels.FunnelObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunnelRepository extends CrudRepository<FunnelObject, Long> {
}
