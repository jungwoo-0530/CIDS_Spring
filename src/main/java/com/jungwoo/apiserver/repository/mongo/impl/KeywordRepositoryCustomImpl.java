package com.jungwoo.apiserver.repository.mongo.impl;

import com.jungwoo.apiserver.repository.mongo.KeywordRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.jungwoo.apiserver.domain.mongo.QKeyword;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * fileName     : KeywordRepositoryCustomImpl
 * author       : jungwoo
 * description  :
 */
public class KeywordRepositoryCustomImpl extends QuerydslRepositorySupport implements KeywordRepositoryCustom {

  private final MongoTemplate mongoTemplate;

  public KeywordRepositoryCustomImpl(MongoOperations mongoOperations, MongoTemplate mongoTemplate) {
    super(mongoOperations.getClass());
    this.mongoTemplate = mongoTemplate;
  }


  //keywords 컬렉션에서 userID의 갯수를 리턴, 중복을 제거.
  @Override
  public Long test() {

    return null;
  }
}
