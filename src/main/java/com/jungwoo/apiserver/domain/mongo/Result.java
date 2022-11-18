package com.jungwoo.apiserver.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Column;

/**
 * fileName     : Result
 * author       : jungwoo
 * description  :
 */

@Data
@Document(collection = "results")
@NoArgsConstructor
@AllArgsConstructor
public class Result {

  @MongoId
  private ObjectId id;

  @Column(name = "keyword_id")
  private String keywordId;

  private String url;

  private Integer label;

  private Float accuracy;

}
