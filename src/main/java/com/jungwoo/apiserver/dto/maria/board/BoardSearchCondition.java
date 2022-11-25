package com.jungwoo.apiserver.dto.maria.board;

import lombok.Builder;
import lombok.Data;

/**
 * fileName     : BoardSearchCondition
 * author       : jungwoo
 * description  :
 */

@Data
@Builder
public class BoardSearchCondition {

  private String option;

  private String keyword;

  private String boardType;

}
