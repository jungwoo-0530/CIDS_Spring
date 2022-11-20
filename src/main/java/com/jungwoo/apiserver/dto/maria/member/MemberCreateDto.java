package com.jungwoo.apiserver.dto.maria.member;


import com.jungwoo.apiserver.exception.validation.ValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * fileName     : CreateMemberRequest
 * author       : jungwoo
 * description  : 회원가입시 요청으로 들어오는 데이터를 바인딩할 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDto {

  @NotBlank(message = "로그인ID는 필수입니다.", groups = ValidationGroup.NotEmptyGroup.class)
  @Size(min = 4, max = 15, message = "아이디를 4~15자리 입력해주세요.", groups = ValidationGroup.SizeGroup.class)
  @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "로그인ID는 특수문자는 포함할 수 없습니다.", groups = ValidationGroup.PatternCheckGroup.class)
  private String loginId;

  @NotBlank(message = "패스워드는 필수입니다.", groups = ValidationGroup.NotEmptyGroup.class)
  @Size(min = 8, max = 16, message = "패스워드를 8~16자리 입력해주세요.", groups = ValidationGroup.SizeGroup.class)
  private String password;

  @NotBlank(message = "이름은 필수입니다.", groups = ValidationGroup.NotEmptyGroup.class)
  private String name;

  @NotBlank(message = "이메일 주소는 필수입니다.", groups = ValidationGroup.NotEmptyGroup.class)
  @Email(message = "이메일 형식에 맞춰서 입력해야합니다.", groups = ValidationGroup.PatternCheckGroup.class)
  private String email;

  @NotBlank(message = "핸드폰 번호는 필수입니다.", groups = ValidationGroup.NotEmptyGroup.class)
  @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "핸드폰 번호 패턴이 맞지 않습니다.", groups = ValidationGroup.PatternCheckGroup.class)
  private String telephone;

}
