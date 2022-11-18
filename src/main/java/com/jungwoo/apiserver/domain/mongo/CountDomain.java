package com.jungwoo.apiserver.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Column;
import java.util.Date;

/**
 * fileName     : CountDomain
 * author       : jungwoo
 * description  :
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "countdomains")
public class CountDomain {

  public static final String HIT = "hit";
  public static final String DOMAIN = "domain";
//  public static final String HIT = "hit";

  @MongoId
  private ObjectId id;

  @Field(name = "url_domain")
  private String domain;

  private Long hit;

  @Field(name = "created_date")
  private Date createDate;

  @Field(name = "updated_date")
  private Date updateDate;
}
