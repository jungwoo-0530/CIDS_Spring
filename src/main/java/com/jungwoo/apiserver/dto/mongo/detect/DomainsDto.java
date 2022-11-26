package com.jungwoo.apiserver.dto.mongo.detect;

import lombok.Builder;
import lombok.Data;

/**
 * fileName     : DomainsDto
 * author       : jungwoo
 * description  :
 */
@Data
@Builder
public class DomainsDto {
  private Integer rank;
  private String domain;
}
