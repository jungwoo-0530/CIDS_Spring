package com.jungwoo.apiserver.dto.maria.member;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * fileName     : CreateMemberRequest
 * author       : jungwoo
 * description  : 회원가입시 요청으로 들어오는 데이터를 바인딩할 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberRequest {

  private String loginId;
  private String password;
  private String name;
  private String email;
  private String telephone;

}
