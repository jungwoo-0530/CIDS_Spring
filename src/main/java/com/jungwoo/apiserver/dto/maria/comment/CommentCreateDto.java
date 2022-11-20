package com.jungwoo.apiserver.dto.maria.comment;

import com.jungwoo.apiserver.domain.maria.Board;
import com.jungwoo.apiserver.domain.maria.Member;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * fileName     : CommentCreateDto
 * author       : jungwoo
 * description  :
 */
@Getter
public class CommentCreateDto {

  @NotBlank(message = "내용은 필수입니다.")
  private String content;
  private Long parentId;
  private Long boardId;
}
