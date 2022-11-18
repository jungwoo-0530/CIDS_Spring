package com.jungwoo.apiserver.dto.maria.comment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.ZonedDateTime;

/**
 * fileName     : CommentPageDto
 * author       : jungwoo
 * description  :
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentPageDto {

  private Long id;

  private boolean available;

  private boolean editable;

  private String author;

  private String content;

  private Long deep;


  private Long parentId;

  private ZonedDateTime createDate;
  private ZonedDateTime updateDate;



}
