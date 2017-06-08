package com.greenfox.kryptonite.projectx.repository;

import com.greenfox.kryptonite.projectx.model.DatabaseStatus;
import org.springframework.data.repository.CrudRepository;

public interface StatusRepository extends CrudRepository<DatabaseStatus, Boolean> {

}
