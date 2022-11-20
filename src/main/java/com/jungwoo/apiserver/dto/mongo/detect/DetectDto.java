package com.jungwoo.apiserver.dto.mongo.detect;

import lombok.*;

import javax.validation.constraints.NotBlank;
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
  @NotBlank(message = "키워드는 필수입니다.")
  String keyword;
  String userId;
  ZonedDateTime createDate;
  ZonedDateTime completeDate;
}
