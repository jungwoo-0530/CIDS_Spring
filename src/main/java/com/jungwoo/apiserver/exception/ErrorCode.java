package com.jungwoo.apiserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * fileName     : ErrorCode
 * author       : jungwoo
 * description  :
 */
public interface ErrorCode {

  HttpStatus getStatus();
  String getMessage();

  String name();
}
