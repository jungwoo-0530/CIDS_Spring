package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.maria.Comment;
import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.maria.comment.CommentPageDto;
import com.jungwoo.apiserver.repository.maria.BoardRepository;
import com.jungwoo.apiserver.repository.maria.CommentRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * fileName     : CommentService
 * author       : jungwoo
 * description  :
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

  private final CommentRepository commentRepository;

  private final BoardRepository boardRepository;


  //ParentComment는 전에 달고 와야함.
  @Transactional
  public void saveHierarchyCommentAndOrders(Comment newComment){
    //부모 Comment 하나만 조회해도 가장 마지막에 삽입된 Comment의 순서 정보(leftNode, rightNode)를 알 수 있다
    //루트 댓글일 경우.
    if (newComment.getParentComment() == null) {

      Comment comment = Comment.builder().
          board(newComment.getBoard()).
          member(newComment.getMember()).
          content(newComment.getContent()).
          depth(1L).
          leftNode(1L).
          rightNode(2L).
          available(true).
          isLastComment(true).build();
      commentRepository.save(comment);

      comment.changeRootComment(comment);

    } else {
      // 어떤 특정한 노드에 하위 노드를 삽입하면,
      // 해당 하위 노드의 leftNode는 상위 노드의 rightNode
      // rightNode는 상위 노드의 rightNode + 1
      Comment comment = Comment.builder().
          member(newComment.getMember()).
          board(newComment.getBoard()).
          content(newComment.getContent()).
          rootComment(newComment.getParentComment().getRootComment()).
          parentComment(newComment.getParentComment()).
          leftNode(newComment.getParentComment().getRightNode()).
          rightNode(newComment.getParentComment().getRightNode() + 1L).
          depth(newComment.getParentComment().getDepth() + 1L).
          available(true).
          isLastComment(true).build();

      newComment.getParentComment().changeLastComment(false);

      commentRepository.adjustHierarchyOrders(comment);
      commentRepository.save(comment);

    }
  }

  @Transactional
  public void initSave(Comment comment) {
//    Comment parentComment = commentRepository.findById(comment.getParentNum()).orElseThrow(NullPointerException::new);
    commentRepository.save(comment);
  }



  @Transactional
  public void updateComment(Comment comment) {
    Comment one = commentRepository.getById(comment.getId());//영속성 컨텍스트
    one.changeComment(comment);
  }

  @Transactional
  public void softDeleteComment(Long commentId) {

    Comment comment = commentRepository.getById(commentId);
    comment.changeAvailableComment(false);
  }



  public Comment findByCommentId(Long parentId) {
    return commentRepository.findById(parentId).orElseThrow(NullPointerException::new);
  }


  public Comment findCommentById(Long id) {
    return commentRepository.findById(id).orElseThrow(NullPointerException::new);
  }

  public Page<CommentPageDto> findCommentsPage(Long boardId, Pageable pageable, Member member) {

    return commentRepository.findCommentsOrderByHierarchy(pageable, boardId, member);
  }


}


