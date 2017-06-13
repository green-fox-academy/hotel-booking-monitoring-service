package com.greenfox.kryptonite.projectx.repository;

import com.greenfox.kryptonite.projectx.model.Heartbeat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartbeatRepository extends CrudRepository<Heartbeat,Boolean> {

}
