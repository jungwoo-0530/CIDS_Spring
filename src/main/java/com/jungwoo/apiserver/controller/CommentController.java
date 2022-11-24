package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.maria.Board;
import com.jungwoo.apiserver.domain.maria.Comment;
import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.BasicResponse;
import com.jungwoo.apiserver.dto.CommonResponse;
import com.jungwoo.apiserver.dto.maria.comment.CommentCreateDto;
import com.jungwoo.apiserver.dto.maria.comment.CommentDto;
import com.jungwoo.apiserver.dto.maria.comment.CommentPageDto;
import com.jungwoo.apiserver.security.jwt.JwtAuthenticationProvider;
import com.jungwoo.apiserver.serviece.BoardService;
import com.jungwoo.apiserver.serviece.CommentService;
import com.jungwoo.apiserver.serviece.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * fileName     : CommentController
 * author       : jungwoo
 * description  :
 */
@Api(tags = "댓글 API Controller")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

  private final CommentService commentService;
  private final MemberService memberService;
  private final BoardService boardService;


  @ApiOperation(value = "게시글 하나에 존재하는 댓글 리스트")
  @GetMapping("/comments")
  public Page<CommentPageDto> listComment(@RequestParam(value = "boardId") Long boardId,
                                          @PageableDefault(size = 10, sort = "id",
                                              direction = Sort.Direction.ASC) Pageable pageable,
                                          HttpServletRequest request) {



    Member member = memberService.getMemberByRequestJwt(request);

    return commentService.findCommentsPage(boardId, pageable, member);


  }



  //CommentForm에서 사용되는 변수
  //BoardId, Content, parentId

  @ApiOperation(value = "댓글 생성")
  @PostMapping("/comments")
  public ResponseEntity<? extends BasicResponse> createComment(@Validated @RequestBody CommentCreateDto commentCreateDto,
                                                               HttpServletRequest request) {

    //1차 쿼리
    Member member = memberService.getMemberByRequestJwt(request);

    //2차 쿼리
    Board board = boardService.getBoardById(commentCreateDto.getBoardId());
    //루트 댓글은 parentComment가 null.
    if (commentCreateDto.getParentId() == null) {
      Comment newComment = Comment.builder()
          .content(commentCreateDto.getContent())
          .member(member)
          .board(board).build();

      commentService.saveHierarchyCommentAndOrders(newComment);
    } else {
      //3차쿼리
      Comment parentComment = commentService.findCommentById(commentCreateDto.getParentId());

      Comment newComment = Comment.builder()
          .content(commentCreateDto.getContent())
          .parentComment(parentComment)
          .member(member)
          .board(board).build();

      commentService.saveHierarchyCommentAndOrders(newComment);
    }

    return ResponseEntity.status(201).body(new CommonResponse<>("댓글 생성 완료"));

  }

  @ApiOperation(value = "댓글 수정(업데이트)")
  @PutMapping("/comments/{commentId}")
  public ResponseEntity<? extends BasicResponse> updateComment(@PathVariable(name = "commentId") Long commentId,
                                                               @Validated @RequestBody CommentDto commentDto) {
    Comment comment = Comment.builder().
        id(commentId).
        content(commentDto.getContent()).build();

    commentService.updateComment(comment);

    return ResponseEntity.ok().body(new CommonResponse<>("댓글을 수정했습니다."));
  }

  @ApiOperation(value = "댓글 삭제")
  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<? extends BasicResponse> deleteComment(@NotBlank @PathVariable(name = "commentId") Long commentId) {

    commentService.softDeleteComment(commentId);

    return ResponseEntity.ok().body(new CommonResponse<>("댓글이 삭제되었습니다."));
  }



}
