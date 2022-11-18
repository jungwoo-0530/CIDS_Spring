package com.jungwoo.apiserver.domain.maria;

import com.jungwoo.apiserver.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

/**
 * fileName     : FileLog
 * author       : jungwoo
 * description  :
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUtil extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "IMAGEUTIL_ID")
  private Long id;

  private boolean useFlag;
  private String fileName;
  private String fileUUID;
  private String filePath;
  private String fileAbsolutePath;
  private String userPk;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOARD_ID")
  private Board board;


  public void changeBoard(Board board) {
    this.board = board;
  }

  public void changeUseFlag(boolean useFlag) {
    this.useFlag = useFlag;
  }

  public void changeFilePath(String filePath) {
    this.filePath = filePath;
  }

  public void changeFileAbsolutePath(String fileAbsolutePath) {
    this.fileAbsolutePath = fileAbsolutePath;
  }
}
