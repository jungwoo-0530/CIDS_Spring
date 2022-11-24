package com.jungwoo.apiserver.domain.maria;

import com.jungwoo.apiserver.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

/**
 * fileName     : Board
 * author       : jungwoo
 * description  :
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOARD_ID")
  private Long id;

  private String title;
  private String content;
  private String type;
  private Integer hit;

  private boolean available;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID")
  private Member member;



  public void plusViewNum(Integer hit) {
    this.hit = hit+1;
  }

  public void changeBoard(Board board) {
    this.title = board.getTitle();
    this.content = board.getContent();
  }

  public void changeAvailableBoard(boolean flag){
    this.available = flag;
  }

  public void changeHtml(String html){
    this.content = html;
  }



}
