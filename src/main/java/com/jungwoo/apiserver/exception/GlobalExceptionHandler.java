package com.jungwoo.apiserver.exception;

import com.jungwoo.apiserver.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * fileName     : GlobalExceptionHandler
 * author       : jungwoo
 * description  :
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e) {
    log.error("handleCustomException: {}", e.getErrorCode());

    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode);
  }


  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
    log.error("handleException: {}", e.getMessage());

    ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
    return handleExceptionInternal(errorCode);
  }

  private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getStatus())
        .body(makeErrorResponse(errorCode));
  }

  private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode, String message) {
    return ResponseEntity.status(errorCode.getStatus().value()).body(makeErrorResponse(errorCode, message));
  }

  private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
    return ErrorResponse.builder()
        .code(errorCode.name())
        .message(errorCode.getMessage())
        .error(errorCode.getStatus().name())
        .status(errorCode.getStatus().value())
        .build();
  }

  private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message){
    return ErrorResponse.builder()
        .code(errorCode.name())
        .message(message)
        .error(errorCode.getStatus().name())
        .status(errorCode.getStatus().value()).build();
  }



}
