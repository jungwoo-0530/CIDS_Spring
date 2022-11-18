package com.jungwoo.apiserver.dto.maria.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.ZonedDateTime;

/**
 * fileName     : BoardPageDto
 * author       : jungwoo
 * description  :
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardPageDto {
  private Long id;
  private String title;
  private String author;
  private String content;
  private String type;
  private Integer hit;
  private boolean available;
  private ZonedDateTime createDate;
  private ZonedDateTime updateDate;



}
