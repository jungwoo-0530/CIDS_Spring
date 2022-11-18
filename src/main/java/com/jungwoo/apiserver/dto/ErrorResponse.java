package com.jungwoo.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jungwoo.apiserver.exception.ErrorCode;
import lombok.*;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

/**
 * fileName     : ErrorResponse
 * author       : jungwoo
 * description  :
 */
@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse extends BasicResponse{

  private final String message;
  private final String code;
  private final String error;
  private final int status;
  private final LocalDateTime timestamp = LocalDateTime.now();

  public ErrorResponse(ErrorCode errorCode) {

    this.message = errorCode.getMessage();
    this.status = errorCode.getStatus().value();
    this.error = errorCode.getStatus().name();
    this.code = errorCode.name();

  }

//  @JsonInclude(JsonInclude.Include.NON_EMPTY)
//  private final List<ValidationError> errors;
//
//  @Getter
//  @Builder
//  @RequiredArgsConstructor
//  public static class ValidationError {
//
//    private final String field;
//    private final String message;
//
//    public static ValidationError of(final FieldError fieldError) {
//      return ValidationError.builder()
//          .field(fieldError.getField())
//          .message(fieldError.getDefaultMessage())
//          .build();
//    }
//  }

}
