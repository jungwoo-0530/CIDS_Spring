package com.jungwoo.apiserver.dto.maria.member;

import com.jungwoo.apiserver.exception.validation.ValidationGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
/**
 * fileName     : MemberUpdateDto
 * author       : jungwoo
 * description  :
 */
@Getter
@Builder
public class MemberUpdateDto {
  @NotBlank(message = "이메일 주소는 필수입니다.", groups = ValidationGroup.NotEmptyGroup.class)
  @Email(message = "이메일 형식에 맞춰서 입력해야합니다.", groups = ValidationGroup.PatternCheckGroup.class)
  private String email;
  @NotBlank(message = "핸드폰 번호는 필수입니다.", groups = ValidationGroup.NotEmptyGroup.class)
  @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "핸드폰 번호 패턴이 맞지 않습니다.", groups = ValidationGroup.PatternCheckGroup.class)
  private String telephone;
}
