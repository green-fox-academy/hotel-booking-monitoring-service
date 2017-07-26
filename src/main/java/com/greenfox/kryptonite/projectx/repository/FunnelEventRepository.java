package com.greenfox.kryptonite.projectx.repository;

import com.greenfox.kryptonite.projectx.model.funnels.FunnelEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunnelEventRepository extends CrudRepository<FunnelEvent, Long> {
}
