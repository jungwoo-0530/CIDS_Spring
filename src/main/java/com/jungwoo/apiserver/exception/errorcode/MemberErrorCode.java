package com.jungwoo.apiserver.exception.errorcode;

import com.jungwoo.apiserver.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * fileName     : MemberErrorCode
 * author       : jungwoo
 * description  :
 */
@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

  MEMBER_PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

  MEMBER_LOGINID_DUPLICATION(HttpStatus.CONFLICT, "중복된 아이디가 존재합니다."),

  MEMBER_EMAIL_DUPLICATION(HttpStatus.CONFLICT, "중복된 이메일이 존재합니다."),

  MEMBER_NO_ACCESS(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

  ;


  private final HttpStatus status;
  private final String message;
}
