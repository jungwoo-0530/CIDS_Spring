package com.jungwoo.apiserver.dto.mongo.countdomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * fileName     : DetectPageDto
 * author       : jungwoo
 * description  :
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountDomainPageDto {
  Long hit;
  String domain;
  Date createDate;
  Date completeDate;
}
