package com.jungwoo.apiserver.security;

import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.repository.maria.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * fileName     : CustomUserDetailsService
 * author       : jungwoo
 * description  : Spring Security에서 유저의 정보를 가져오는 인터페이스(UserDetailsService)의 구현체.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

    Member member = getMember(loginId);

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole()));

    return new CustomUserDetails(member, authorities);
  }

  private Member getMember(String loginId) {
    return memberRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("not found loginId : " + loginId));
  }




//  public static boolean hasAdminRole() {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//    return authorities.stream().filter((o) -> {
//      return o.getAuthority().equals("ROLE_ADMIN");
//    }).findAny().isPresent();
//  }

}
