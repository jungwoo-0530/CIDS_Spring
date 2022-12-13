package com.jungwoo.apiserver.dto.maria.board;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * fileName     : BoardCreateDto
 * author       : jungwoo
 * description  :
 */
@Getter
public class BoardCreateDto {
  @NotBlank(message = "제목은 필수입니다.")
  private String title;
  @NotBlank(message = "내용은 필수입니다.")
  private String content;
  private String type;
}

//{
//  "title": "",
//  "content": "",
//  "type": ""
//}
