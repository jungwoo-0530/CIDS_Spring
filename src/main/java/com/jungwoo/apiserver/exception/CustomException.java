package com.jungwoo.apiserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * fileName     : CustomException
 * author       : jungwoo
 * description  :
 */

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
  private final ErrorCode errorCode;
}
