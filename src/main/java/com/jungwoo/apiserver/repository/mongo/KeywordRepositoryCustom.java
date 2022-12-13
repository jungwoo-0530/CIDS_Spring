package com.jungwoo.apiserver.repository.mongo;

import com.jungwoo.apiserver.dto.mongo.detect.SearchKeywordDto;

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
