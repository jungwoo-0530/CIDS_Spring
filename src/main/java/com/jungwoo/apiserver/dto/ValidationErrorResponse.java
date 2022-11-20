package com.jungwoo.apiserver.dto;

import com.mysema.commons.lang.Pair;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * fileName     : ValidationErrorResponse
 * author       : jungwoo
 * description  :
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class ValidationErrorResponse extends BasicResponse{
  private final Map<String, String> messages;
  private final String error;
  private final int status;

  private final LocalDateTime timestamp = LocalDateTime.now();

}
