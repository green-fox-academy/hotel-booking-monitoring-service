package com.greenfox.kryptonite.projectx.repository;


import com.greenfox.kryptonite.projectx.model.pageviews.PageViewData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageViewDataRepository extends PagingAndSortingRepository<PageViewData, Long> {

}
