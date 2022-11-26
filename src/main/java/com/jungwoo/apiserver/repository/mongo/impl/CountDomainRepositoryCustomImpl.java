package com.jungwoo.apiserver.repository.mongo.impl;

import com.jungwoo.apiserver.domain.mongo.CountDomain;
import com.jungwoo.apiserver.domain.mongo.QCountDomain;
import com.jungwoo.apiserver.dto.mongo.countdomain.CountDomainPageDto;
import com.jungwoo.apiserver.dto.mongo.detect.DomainsDto;
import com.jungwoo.apiserver.repository.mongo.CountDomainRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * fileName     : CountDomainRepositoryCustomImpl
 * author       : jungwoo
 * description  :
 */
//@Component

@Slf4j
public class CountDomainRepositoryCustomImpl extends QuerydslRepositorySupport implements CountDomainRepositoryCustom {
    private final MongoTemplate mongoTemplate;

  public CountDomainRepositoryCustomImpl(MongoOperations mongoOperations, MongoTemplate mongoTemplate) {
    super(mongoOperations.getClass());
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public List<DomainsDto> getTop5DomainsOrderByHit() {

    ProjectionOperation projectionOperation = Aggregation.project().and("domain").as("domain")
        .and("hit").as("hit").andExclude("_id");
    SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC,"hit");
    LimitOperation limitOperation = Aggregation.limit(5);

    Aggregation aggregation = Aggregation.newAggregation(projectionOperation, sortOperation, limitOperation);

    AggregationResults<DomainsDto> aggregate = this.mongoTemplate.aggregate(aggregation, CountDomain.class, DomainsDto.class);

    List<DomainsDto> mappedResults = aggregate.getMappedResults();

    Integer i = 1;
    for(DomainsDto d : mappedResults){
      d.setRank(i);
      i++;
    }

    return mappedResults;
  }


  @Override
  public Page<CountDomain> findCountDomainPaging(Pageable pageable) {

    Query query = new Query();

    query.with(Sort.by(Sort.Direction.DESC, "hit"));

    query.with(pageable);

    List<CountDomain> result = this.mongoTemplate.find(query, CountDomain.class);

    return PageableExecutionUtils.getPage(
        result,
        pageable,
        ()->mongoTemplate.count(Query.of(query).limit(-1).skip(-1), CountDomain.class)
    );
  }
}
