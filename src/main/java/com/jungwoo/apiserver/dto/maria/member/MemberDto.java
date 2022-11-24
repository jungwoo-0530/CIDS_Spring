package com.jungwoo.apiserver.dto.maria.member;

import lombok.Builder;
import lombok.Data;

/**
 * fileName     : MemberDto
 * author       : jungwoo
 * description  :
 */
@Data
@Builder
public class MemberDto {
  private Long id;
  private String name;
  private String loginId;
  private String email;
  private String telephone;
  private String role;
  private String imgUri;
}
