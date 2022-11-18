package com.jungwoo.apiserver.repository.maria;

import com.jungwoo.apiserver.domain.maria.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * fileName     : BoardRepository
 * author       : jungwoo
 * description  :
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {


  @Query("select b from Board b left join fetch b.member where b.id = :id")
  Optional<Board> findByBoardIdWithMember(@Param("id") Long boardId);


  @Modifying
  @Query("update Board b set b.hit = b.hit+1 where b.id = :id")
  void addViewHit(@Param("id") Long boardId);
}
