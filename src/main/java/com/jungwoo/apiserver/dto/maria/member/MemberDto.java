package com.jungwoo.apiserver.dto.maria.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * fileName     : MemberDto
 * author       : jungwoo
 * description  :
 */
@Getter
@Builder
@AllArgsConstructor
public class MemberDto {

  private String loginId;
  private String password;
  private String name;
  private String email;
  private String telephone;
  private String role;

}
