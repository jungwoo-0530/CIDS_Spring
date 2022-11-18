package com.jungwoo.apiserver.repository.maria;

import com.jungwoo.apiserver.domain.maria.ImageUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * fileName     : FileLogRepository
 * author       : jungwoo
 * description  :
 */
@Repository
public interface ImageUtilRepository extends JpaRepository<ImageUtil, Long> {



  @Query("select i from ImageUtil i where i.userPk = :loginId and i.useFlag = false")
  List<ImageUtil> findAllByLoginId(@Param("loginId") String loginId);

  @Query("delete from ImageUtil i where i.userPk = :loginId")
  void deleteByLoginId(@Param("loginId") String loginId);
}
