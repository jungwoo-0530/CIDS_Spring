package com.jungwoo.apiserver.repository.maria;

import com.jungwoo.apiserver.dto.maria.board.BoardPageDto;
import com.jungwoo.apiserver.dto.maria.board.BoardSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * fileName     : BoardRepositoryCustom
 * author       : jungwoo
 * description  :
 */
public interface BoardRepositoryCustom {
  Page<BoardPageDto> findAllPageSort(String boardType, Pageable pageable);

  Page<BoardPageDto> findAllPageByKeyword(BoardSearchCondition condition, Pageable pageable);
}
