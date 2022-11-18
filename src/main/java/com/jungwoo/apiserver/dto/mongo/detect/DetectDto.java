package com.jungwoo.apiserver.dto.mongo.detect;

import lombok.*;

import java.time.ZonedDateTime;

/**
 * fileName     : detectDto
 * author       : jungwoo
 * description  :
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetectDto {

  String email;
  String keyword;
  String userId;
  ZonedDateTime createDate;
  ZonedDateTime completeDate;
}
