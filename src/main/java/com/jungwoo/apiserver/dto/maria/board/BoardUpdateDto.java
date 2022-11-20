package com.jungwoo.apiserver.dto.maria.board;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * fileName     : BoardUpdateDto
 * author       : jungwoo
 * description  :
 */
@Getter
public class BoardUpdateDto {

  @NotBlank(message = "제목은 필수입니다.")
  private String title;
  @NotBlank(message = "제목은 필수입니다.")
  private String content;
}
