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

  private Long rank;
  private String keyword;

}
