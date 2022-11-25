package com.jungwoo.apiserver.repository.mongo;

import com.jungwoo.apiserver.domain.mongo.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * fileName     : ResultRepository
 * author       : jungwoo
 * description  :
 */

@Repository
public interface ResultRepository extends MongoRepository<Result, String>{

  @Query("{'keyword_id': :#{#keywordId}, 'label': :#{#label}}")
  List<Result> findAllByKeywordIdAndLabel(@Param("keywordId") String keywordId, @Param("label")Integer label);

  List<Result> findAllByLabel(Integer label);
}
