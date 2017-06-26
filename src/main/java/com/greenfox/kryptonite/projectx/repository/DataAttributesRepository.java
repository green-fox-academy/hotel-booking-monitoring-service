package com.greenfox.kryptonite.projectx.repository;


import com.greenfox.kryptonite.projectx.model.pageviews.DataAttributes;
import com.greenfox.kryptonite.projectx.model.pageviews.PageViewData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataAttributesRepository extends PagingAndSortingRepository<DataAttributes, Long> {
}
