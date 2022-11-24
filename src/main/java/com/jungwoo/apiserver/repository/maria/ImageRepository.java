package com.jungwoo.apiserver.repository.maria;

import com.jungwoo.apiserver.domain.maria.Image;
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
public interface ImageRepository extends JpaRepository<Image, Long> {



  @Query("select i from Image i where i.loginId = :loginId and i.isUse = false")
  List<Image> findAllByLoginId(@Param("loginId") String loginId);

  @Query("delete from Image i where i.loginId = :loginId")
  void deleteByLoginId(@Param("loginId") String loginId);


  @Query("select i from Image i where i.loginId = :loginId and i.fileRelativePath like %:uri")
  Image findOneByLoginIdAndRelativePath(@Param("loginId")String loginId, @Param("uri") String uri);
}
