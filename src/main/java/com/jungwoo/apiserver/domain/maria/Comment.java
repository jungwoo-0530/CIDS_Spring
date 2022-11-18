package com.jungwoo.apiserver.domain.maria;

import com.jungwoo.apiserver.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * fileName     : Comment
 * author       : jungwoo
 * description  :
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "COMMENT_ID")
  private Long id;

  private String content;

  private boolean available;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOARD_ID")
  private Board board;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROOT_COMMENT_ID")
  private Comment rootComment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PARENT_COMMENT_ID")
  private Comment parentComment;

  @Column(nullable = false)
  private Long leftNode = 1L;

  @Column(nullable = false)
  private Long rightNode = 2L;

  @Column(nullable = false)
  private Long depth = 1L;

  @Column(nullable = false)
  private boolean isLastComment;

  public void updateChildComment(Comment childComment) {

    if (this.depth >= 99) {
      throw new IllegalArgumentException();
    }
    childComment.rootComment = this.rootComment;
    childComment.parentComment = this;
    childComment.depth = this.depth + 1L;
    childComment.leftNode = this.rightNode;
    childComment.rightNode = this.rightNode + 1;
  }


  public boolean getAvailable(){
    return this.available;
  }


  public boolean isLastComment() {
    return isLastComment;
  }

  //==수정==/
  public void changeComment(Comment comment) {
    this.content = comment.content;
  }

  //==삭제==//
  public void changeAvailableComment(boolean available) {
    this.available = available;
  }

  public void changeLastComment(boolean lastComment) {
    this.isLastComment = lastComment;
  }

  public void changeRootComment(Comment comment) {
    this.rootComment = comment;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Comment comment = (Comment) o;
    return available == comment.available && Objects.equals(id, comment.id) && Objects.equals(content, comment.content) && Objects.equals(board, comment.board) && Objects.equals(member, comment.member) && Objects.equals(rootComment, comment.rootComment) && Objects.equals(parentComment, comment.parentComment) && Objects.equals(leftNode, comment.leftNode) && Objects.equals(rightNode, comment.rightNode) && Objects.equals(depth, comment.depth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, content, available, board, member, rootComment, parentComment, leftNode, rightNode, depth);
  }
}
