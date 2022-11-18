package com.jungwoo.apiserver.security;

import java.util.Collection;

import com.jungwoo.apiserver.domain.maria.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
/**
 * fileName     : CustomUserDetails
 * author       : jungwoo
 * description  : 사용자의 정보를 담는 인터페이스(UserDetails) 구현체
 */
public class CustomUserDetails extends User {
  private final Member member;

  public CustomUserDetails(Member member, Collection<? extends GrantedAuthority> authorities) {
    super(member.getLoginId(), member.getPassword(), authorities);
    this.member = member;
  }

  @Override
  public String getPassword() {
    return super.getPassword();
  }


  //핵심 메서드.
  //DB PK값
  @Override
  public String getUsername() {
    return super.getUsername();
  }

  @Override
  public boolean isEnabled() {
    return super.isEnabled();
  }

  @Override
  public boolean isAccountNonExpired() {
    return super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return super.isCredentialsNonExpired();
  }

  public Member getMember() {
    return this.member;
  }
}
