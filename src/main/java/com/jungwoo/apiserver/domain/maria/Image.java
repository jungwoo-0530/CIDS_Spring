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
public class Image extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "IMAGE_ID")
  private Long id;

  private boolean isUse;
  private String fileName;
  private String fileUUID;
  private String fileRelativePath;
  private String fileAbsolutePath;
  private String loginId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOARD_ID")
  private Board board;


  public void changeFileAbsolutePath(String fileAbsolutePath) {
    this.fileAbsolutePath = fileAbsolutePath;
  }


  public void tempToPersistBoardImage(Board board, boolean useFlag, String imageAbsoluteUri, String imageRelativeUri) {
    this.board = board;
    this.isUse = useFlag;
    this.fileRelativePath = imageRelativeUri;
    this.fileAbsolutePath = imageAbsoluteUri;
  }
}
