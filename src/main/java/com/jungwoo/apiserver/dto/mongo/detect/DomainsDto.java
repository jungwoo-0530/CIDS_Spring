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
  private Long rank;
  private String domain;
}
