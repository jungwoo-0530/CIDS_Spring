package com.jungwoo.apiserver.dto.maria.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * fileName     : MemberPageDto
 * author       : jungwoo
 * description  :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPageDto {

  private Long id;

  private String loginId;

  private String name;

  private String role;

  private ZonedDateTime createDate;


}
