package com.jungwoo.apiserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * fileName     : BoardErrorCode
 * author       : jungwoo
 * description  :
 */
@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode{

  POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글 정보를 찾을 수 없습니다."),

  POST_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "게시글을 저장할 수 없습니다.")


  ;

  private final HttpStatus status;
  private final String message;

}
