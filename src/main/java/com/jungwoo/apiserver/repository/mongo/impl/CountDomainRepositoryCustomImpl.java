package com.jungwoo.apiserver.repository.mongo.impl;

import com.jungwoo.apiserver.domain.mongo.CountDomain;
import com.jungwoo.apiserver.domain.mongo.QCountDomain;
import com.jungwoo.apiserver.repository.mongo.CountDomainRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.querydsl.QSort;

import java.util.Iterator;
import java.util.List;

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

}
