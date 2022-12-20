package com.jungwoo.apiserver.exception.errorcode;

import com.jungwoo.apiserver.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * fileName     : ImageUtilErrorCode
 * author       : jungwoo
 * description  :
 */
@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {

  TEMP_IMAGE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지를 저장할 수 없습니다."),

  TEMP_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),

  IMAGE_DIRECTORY_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장 디렉토리를 생성할 수 없습니다.")
  ;

  private final HttpStatus status;
  private final String message;
}
