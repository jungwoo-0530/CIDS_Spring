package com.jungwoo.apiserver.dto.mongo.detect;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * fileName     : dashboardDto
 * author       : jungwoo
 * description  :
 */
@Getter
@Builder
public class DashboardDto {

  private Integer keywordCount;

  private Integer memberCount;

  private Double averageAccuracy;

  private List<SearchKeywordDto> searchKeywords;

  private List<DomainsDto> domains;

}
