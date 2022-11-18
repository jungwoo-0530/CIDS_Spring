package com.jungwoo.apiserver.dto.maria.comment;

import com.jungwoo.apiserver.domain.maria.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * fileName     : CommentViewDto
 * author       : jungwoo
 * description  :
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentViewDto implements Serializable {

  private Long id;
  private String content;
  private Long memberId;
  private List<CommentViewDto> children = new ArrayList<>();

  public CommentViewDto(Long id, String content, Long memberId) {
    this.id = id;
    this.content = content;
    this.memberId = memberId;
  }

  public static CommentViewDto convertCommentToDto(Comment comment) {
    return !comment.getAvailable() ?
        new CommentViewDto(comment.getId(), "삭제된 댓글입니다.", null, null) :
        new CommentViewDto(comment.getId(), comment.getContent(), comment.getMember().getId());
  }
}
