package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.maria.Board;
import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.*;
import com.jungwoo.apiserver.dto.maria.board.BoardCreateDto;
import com.jungwoo.apiserver.dto.maria.board.BoardPageDto;
import com.jungwoo.apiserver.dto.maria.board.BoardSearchCondition;
import com.jungwoo.apiserver.dto.maria.board.BoardUpdateDto;
import com.jungwoo.apiserver.exception.BoardErrorCode;
import com.jungwoo.apiserver.exception.CustomException;
import com.jungwoo.apiserver.serviece.ImageUtilService;
import com.jungwoo.apiserver.serviece.MemberService;
import com.jungwoo.apiserver.serviece.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * fileName     : BoardController
 * author       : jungwoo
 * description  :
 */
@Api(tags = "게시글 API 정보를 제공하는 Controller")
@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;
  private final MemberService memberService;

  @ApiOperation(value = "카테고리에 맞는 게시글 목록을 반환하는 메소드")
  @ApiImplicitParam(name = "type", value = "게시글 카테고리", dataType = "String")
  @GetMapping("/boards")
  public Page<BoardPageDto> listBoard(@RequestParam(value = "boardType") String boardType,
                                      @PageableDefault(size = 10, sort = "id",
                                          direction = Sort.Direction.DESC) Pageable pageable) {
    log.info("BoardController getmapping list");
    log.info("{}", memberService.getClass());



    return boardService.findPageSort(boardType, pageable);
  }

  @ApiOperation(value = "게시글 하나를 읽는 메소드")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "boardId", value = "게시글 아이디", paramType = "path", dataType = "Long"),
  })
  @GetMapping("/boards/{boardId}")
  public ResponseEntity<? extends BasicResponse> readBoard(@PathVariable(name = "boardId") Long boardId,
                                                           HttpServletRequest request) {
    log.info("BoardController readBoard");

    Board board = boardService.getBoardAndAddHit(boardId);

    boolean editable = boardService.isAuthorityAtBoardUpdateAndDelete(request, board);


    BoardDto boardDto = BoardDto.builder().
        content(board.getContent()).
        title(board.getTitle()).
        author(board.getMember().getLoginId()).
        type(board.getType()).
        email(board.getMember().getEmail()).
        available(board.isAvailable()).
        editable(editable).
        updateTime(board.getUpdateDate()).
        createTime(board.getCreateDate()).
        build();


    return ResponseEntity.ok().body(new CommonResponse<>(boardDto, "게시물을 불러왔습니다."));

  }

  @Getter
  @Builder
  private static class BoardDto {
    Long id;
    String title;
    String content;
    String author;
    String type;
    String email;
    Member member;
    boolean available;
    boolean editable;
    ZonedDateTime createTime;
    ZonedDateTime updateTime;
  }


  @SneakyThrows
  @ApiOperation(value = "게시글을 생성하는 메소드")
  @ApiImplicitParam(name = "boardType", value = "게시글 카테고리")
  @PostMapping("/boards")
  public ResponseEntity<? extends BasicResponse> createBoard(@RequestBody BoardCreateDto boardCreateDto,
                                                             HttpServletRequest request) throws IOException {

    Member member = memberService.getMemberByRequestJwt(request);

    Board board = Board.builder().
        title(boardCreateDto.getTitle()).
        content(boardCreateDto.getContent()).
        member(member).
        type(boardCreateDto.getType()).
        hit(1).
        available(true).
        build();


    Long id = boardService.saveBoard(board);

    return ResponseEntity.status(201).body(new CommonResponse<>(id, "게시물을 생성했습니다."));
  }


  @DeleteMapping("/boards/{boardId}")
  public ResponseEntity<? extends BasicResponse> deleteBoard(@PathVariable(name = "boardId") Long boardId, HttpServletRequest request) {



    boardService.softDeleteBoard(boardId, request);

    return ResponseEntity.ok().body(new CommonResponse<>("게시물을 삭제했습니다."));

  }

  @PutMapping("/boards/{boardId}")
  public ResponseEntity<? extends BasicResponse> updateBoard(@PathVariable(name = "boardId") Long boardId,
                                                            @Validated @RequestBody BoardUpdateDto boardUpdateDto,
                                                             HttpServletRequest request) {

    log.info("BoardController updateBoard");


    Board newBoard = Board.builder().
        id(boardId).
        title(boardUpdateDto.getTitle()).
        content(boardUpdateDto.getContent()).build();


    boardService.updateBoard(newBoard, request);

    return ResponseEntity.ok().body(new CommonResponse<>("게시물을 수정했습니다."));
  }


  @GetMapping("/boards/search")
  public Page<BoardPageDto> listBoardBySearch(@RequestBody BoardSearchCondition condition, @PageableDefault(size = 2, sort = "id",
                                              direction = Sort.Direction.DESC) Pageable pageable) {

    return boardService.findAllPageBySearch(condition, pageable);
  }

  //글쓰기 클릭시 인증
  //공지사항은 admin만 qna는 모두다.
  @GetMapping("/boards/authUser/{boardType}")
  public ResponseEntity<? extends BasicResponse> authUser(@PathVariable(name = "boardType") String boardType,
                                                          HttpServletRequest request) {

    Member member = memberService.getMemberByRequestJwt(request);
    boardService.checkAuthorityAtBoardWrite(boardType, member);

    System.out.println(boardType + " " + member.getRole());

    return ResponseEntity.ok().body(new CommonResponse<>("권한이 있습니다."));
  }




}
