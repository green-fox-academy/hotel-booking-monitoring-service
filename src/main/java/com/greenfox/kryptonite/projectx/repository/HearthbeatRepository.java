package com.greenfox.kryptonite.projectx.repository;

import com.greenfox.kryptonite.projectx.model.Hearthbeat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HearthbeatRepository extends CrudRepository<Hearthbeat,Boolean> {

}
