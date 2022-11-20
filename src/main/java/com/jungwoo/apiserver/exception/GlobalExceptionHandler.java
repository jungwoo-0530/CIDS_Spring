package com.jungwoo.apiserver.exception;

import com.jungwoo.apiserver.dto.ErrorResponse;
import com.jungwoo.apiserver.dto.ValidationErrorResponse;
import com.jungwoo.apiserver.exception.validation.ValidationCustomException;
import com.mysema.commons.lang.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fileName     : GlobalExceptionHandler
 * author       : jungwoo
 * description  :
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
    log.error("handleException: {}", e.getMessage());

    ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
    return handleExceptionInternal(errorCode);
  }

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e) {
    log.error("handleCustomException: {}", e.getErrorCode());

    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode);
  }


  private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getStatus())
        .body(makeErrorResponse(errorCode));
  }

  private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
    return ErrorResponse.builder()
        .code(errorCode.name())
        .message(errorCode.getMessage())
        .error(errorCode.getStatus().name())
        .status(errorCode.getStatus().value())
        .build();
  }

  private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode, String message) {
    return ResponseEntity.status(errorCode.getStatus().value()).body(makeErrorResponse(errorCode, message));
  }
  private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message){
    return ErrorResponse.builder()
        .code(errorCode.name())
        .message(message)
        .error(errorCode.getStatus().name())
        .status(errorCode.getStatus().value()).build();
  }

  //////////////////////////////////////////Validation///////////////////////////////////////////////


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.error(e.getMessage());
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return ResponseEntity.status(400).body(
        ValidationErrorResponse.builder()
            .status(400)
            .error("BAD_REQUEST")
            .messages(errors)
            .build()
    );
  }
}
