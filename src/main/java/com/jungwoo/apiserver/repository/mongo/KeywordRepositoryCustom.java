package com.jungwoo.apiserver.repository.mongo;

import com.jungwoo.apiserver.domain.mongo.Keyword;
import com.jungwoo.apiserver.dto.mongo.detect.SearchKeywordDto;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * fileName     : KeywordRepositoryCustom
 * author       : jungwoo
 * description  :
 */
public interface KeywordRepositoryCustom {

  Integer countKeywordCount();

  Integer getMemberAtKeywords();

  List<SearchKeywordDto> getKeywordsOrderByDesc();

}
