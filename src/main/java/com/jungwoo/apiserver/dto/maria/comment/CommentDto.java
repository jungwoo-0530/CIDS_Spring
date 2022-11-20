package com.jungwoo.apiserver.dto.maria.comment;

import com.jungwoo.apiserver.domain.maria.Board;
import com.jungwoo.apiserver.domain.maria.Comment;
import com.jungwoo.apiserver.domain.maria.Member;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * fileName     : CommentDto
 * author       : jungwoo
 * description  :
 */
@Builder
@Data
public class CommentDto {

  private Long id;
  @NotBlank(message = "내용은 필수입니다.")
  private String content;
  private Long boardId;
  private Long parentId;

  private Board board;
  private Member member;

  private Comment parentComment;
  private Comment rootComment;

}
