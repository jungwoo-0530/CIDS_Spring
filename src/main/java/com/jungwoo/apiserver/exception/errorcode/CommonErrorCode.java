package com.jungwoo.apiserver.exception.errorcode;

import com.jungwoo.apiserver.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * fileName     : CommonErrorCode
 * author       : jungwoo
 * description  :
 */
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

  RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),

  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
  ;

  private final HttpStatus status;
  private final String message;

}
