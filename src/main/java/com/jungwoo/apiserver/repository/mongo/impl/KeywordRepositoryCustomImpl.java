package com.jungwoo.apiserver.repository.mongo.impl;

import com.jungwoo.apiserver.domain.mongo.Keyword;
import com.jungwoo.apiserver.dto.mongo.detect.SearchKeywordDto;
import com.jungwoo.apiserver.repository.mongo.KeywordRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * fileName     : KeywordRepositoryCustomImpl
 * author       : jungwoo
 * description  :
 */
@Slf4j
public class KeywordRepositoryCustomImpl extends QuerydslRepositorySupport implements KeywordRepositoryCustom {

  private final MongoTemplate mongoTemplate;



  public KeywordRepositoryCustomImpl(MongoOperations mongoOperations, MongoTemplate mongoTemplate) {
    super(mongoOperations.getClass());
    this.mongoTemplate = mongoTemplate;
  }


  //keywords 컬렉션에서 keyword의 갯수를 리턴, 중복을 제거.
  @Override
  public Integer countKeywordCount() {

    GroupOperation groupOperation = Aggregation.group("keyword");
    Aggregation aggregation = Aggregation.newAggregation(groupOperation);
    AggregationResults<String> results = this.mongoTemplate.aggregate(aggregation, Keyword.class, String.class);


    return results.getMappedResults().size();
  }


  //리턴 : 모든 userId를 뽑아옴. 중복 제거하고 몇개인지 리턴
  @Override
  public Integer getMemberAtKeywords() {

    GroupOperation groupOperation = Aggregation.group("userId");
    Aggregation aggregation = Aggregation.newAggregation(groupOperation);
    AggregationResults<String> results = this.mongoTemplate.aggregate(aggregation, Keyword.class, String.class);

    return results.getMappedResults().size();
  }

  //모든 Keywords 다큐먼트를 가져옴. 중복 카운트 해서 내림차순으로 정렬. 5개 리밋.
  @Override
  public List<SearchKeywordDto> getKeywordsOrderByDesc() {

    GroupOperation groupOperation = Aggregation.group("keyword").count().as("cnt");
    ProjectionOperation projectionOperation = Aggregation.project().andExclude("_id").and("_id").as("keyword").and("cnt").as("cnt");
    SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "cnt");
    LimitOperation limitOperation = Aggregation.limit(5);

    Aggregation aggregation = Aggregation.newAggregation(groupOperation, projectionOperation, sortOperation, limitOperation);

    AggregationResults<SearchKeywordDto> aggregate = this.mongoTemplate.aggregate(aggregation, Keyword.class, SearchKeywordDto.class);

    List<SearchKeywordDto> results = aggregate.getMappedResults();

    Integer i = 1;
    for (SearchKeywordDto d : results) {
      d.setRank(i);
      i++;
    }

    return results;
  }
}
