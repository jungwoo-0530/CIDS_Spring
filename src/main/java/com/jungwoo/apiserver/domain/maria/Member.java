package com.jungwoo.apiserver.domain.maria;

import com.jungwoo.apiserver.domain.BaseTimeEntity;
import lombok.*;
import javax.persistence.*;
/**
 * fileName     : Member
 * author       : jungwoo
 * description  : 회원 Entity
 */

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MEMBER_ID")
  private Long id;

  private String loginId;
  private String password;
  private String name;
  private String email;
  private String telephone;
  private String role;

  private String imgUri;

  public void changeTelephoneAndEmail(String email, String telephone){
    this.email = email;
    this.telephone = telephone;
  }

  public void changeRole(String role) {
    this.role = role;
  }

  public void changePassword(String password) {
    this.password = password;
  }

  public void changeImgUri(String imgUri){
    this.imgUri = imgUri;
  }


}
