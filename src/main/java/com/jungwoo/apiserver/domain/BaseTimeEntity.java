package com.jungwoo.apiserver.domain;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZonedDateTime;
/**
 * fileName     : BaseTimeEntity
 * author       : jungwoo
 * description  : Jpa Auditing를 활용하여 DB에 생성시 create시간과 Update 시간을 자동으로 insert해준다.
 */
@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {

  private ZonedDateTime createDate;
  private ZonedDateTime updateDate;

  @PrePersist
  public void prePersist(){
    this.createDate = ZonedDateTime.now();
    this.updateDate = ZonedDateTime.now();
  }

  @PreUpdate
  public void postUpdate() {
    this.updateDate = ZonedDateTime.now();
  }

}
