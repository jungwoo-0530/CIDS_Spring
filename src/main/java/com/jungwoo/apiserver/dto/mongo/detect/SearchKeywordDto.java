package com.jungwoo.apiserver.dto.mongo.detect;

import lombok.Builder;
import lombok.Data;

/**
 * fileName     : searchKeywordDto
 * author       : jungwoo
 * description  :
 */
@Data
@Builder
public class SearchKeywordDto {

  private Integer rank;
  private String keyword;

}
