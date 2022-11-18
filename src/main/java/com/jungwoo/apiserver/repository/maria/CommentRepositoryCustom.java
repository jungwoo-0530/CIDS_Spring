package com.jungwoo.apiserver.repository.maria;

import com.jungwoo.apiserver.domain.maria.Comment;
import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.maria.comment.CommentPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * fileName     : CommentRepositoryCustom
 * author       : jungwoo
 * description  :
 */
public interface CommentRepositoryCustom {


  public Page<CommentPageDto> findCommentsOrderByHierarchy(Pageable pageable, Long boardId, Member member);

  public void adjustHierarchyOrders(Comment newComment);

}
