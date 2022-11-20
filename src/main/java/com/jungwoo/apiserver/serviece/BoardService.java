package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.maria.Board;
import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.maria.board.BoardPageDto;
import com.jungwoo.apiserver.dto.maria.board.BoardSearchCondition;
import com.jungwoo.apiserver.exception.BoardErrorCode;
import com.jungwoo.apiserver.exception.CustomException;
import com.jungwoo.apiserver.exception.MemberErrorCode;
import com.jungwoo.apiserver.repository.maria.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * fileName     : BoardService
 * author       : jungwoo
 * description  :
 */
@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;

  private final MemberService memberService;
  private final ImageUtilService imageUtilService;




  @Transactional(readOnly = true)
  public Page<BoardPageDto> findPageSort(String boardType, Pageable pageable) {
    return boardRepository.findAllPageSort(boardType, pageable);
  }


  @Transactional
  public Long saveBoard(Board board) throws IOException {

    try {
      boardRepository.save(board);

      imageUtilService.permanentSaveImage(board.getMember().getLoginId(), board);

    } catch (IOException e) {
      throw new CustomException(BoardErrorCode.POST_SAVE_FAILED);
    }


    return board.getId();
  }

  @Transactional(readOnly = true)
  public Board getBoardById(Long boardId) {
    return boardRepository.findById(boardId).orElseThrow(()->new CustomException(BoardErrorCode.POSTS_NOT_FOUND));
  }



  @Transactional
  public Board getBoardAndAddHit(Long boardId) {

    Board board = boardRepository.findByBoardIdWithMember(boardId).orElseThrow(()->new CustomException(BoardErrorCode.POSTS_NOT_FOUND));

    boardRepository.addViewHit(boardId);

    return board;
  }

  public Page<BoardPageDto> findAllPageBySearch(BoardSearchCondition condition, Pageable pageable) {
    return boardRepository.findAllPageByKeyword(condition, pageable);
  }

  @Transactional
  public void softDeleteBoard(Long boardId, HttpServletRequest request) {
    Board one = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(BoardErrorCode.POSTS_NOT_FOUND));
    if(!isAuthorityAtBoardUpdateAndDelete(request, one))
      throw new CustomException(MemberErrorCode.MEMBER_NO_ACCESS);
    one.changeAvailableBoard(false);
  }


  @Transactional
  public void updateBoard(Board board, HttpServletRequest request) {
    Board one = boardRepository.findById(board.getId()).orElseThrow(()->new CustomException(BoardErrorCode.POSTS_NOT_FOUND));
    if(!isAuthorityAtBoardUpdateAndDelete(request, one))
      throw new CustomException(MemberErrorCode.MEMBER_NO_ACCESS);
    one.changeBoard(board);
  }


  ////////////////////////////Security////////////////////////////////

  //글 수정, 삭제 권한 검사
  public boolean isAuthorityAtBoardUpdateAndDelete(HttpServletRequest request, Board board) {


    Member member = memberService.getMemberByRequestJwt(request);

    if (member.getRole().equals("ADMIN") || board.getMember().getId().equals(member.getId())) {
      return true;
    } else {
      return false;
    }

  }

  //boardType에 따른 글쓰기 권한
  public void checkAuthorityAtBoardWrite(String boardType, Member member) {

    if (member.getRole().equals("ADMIN") || (boardType.equals("qna") && member.getRole().equals("MEMBER"))) {
      return;
    }else{
      throw new CustomException(MemberErrorCode.MEMBER_NO_ACCESS);
    }

  }


}
