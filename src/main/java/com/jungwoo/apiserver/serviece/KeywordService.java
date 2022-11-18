package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.mongo.Keyword;
import com.jungwoo.apiserver.repository.mongo.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * fileName     : KeywordService
 * author       : jungwoo
 * description  :
 */

@Service
@RequiredArgsConstructor
public class KeywordService {

  private final KeywordRepository keywordRepository;




  @Transactional
  public Keyword save(String keyword){
    Keyword build = Keyword.builder()
        .keyword(keyword)
        .status(1)
        .updateDate(Date.from(ZonedDateTime.now().toInstant()))
        .createDate(Date.from(ZonedDateTime.now().toInstant()))
        .build();
    return keywordRepository.save(build);
  }



}
