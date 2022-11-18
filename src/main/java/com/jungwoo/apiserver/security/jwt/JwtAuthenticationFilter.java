package com.jungwoo.apiserver.security.jwt;

import com.jungwoo.apiserver.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;

/**
 * fileName     : JwtAuthenticationFilter
 * author       : jungwoo
 * description  : 인터셉터. 클라이언트의 모든 요청에 대해 intercept하여 인증에 성공하면 authentication객체를 Context안에 넣는다. 인증에 실패하면 아무런 과정 없이 다음 필터로 넘어간다.
 */

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtAuthenticationProvider jwtAuthenticationProvider;



  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("JwtFilter start");
    String token = jwtAuthenticationProvider.getTokenInRequestHeader(request, "Bearer");



    if(token != null && jwtAuthenticationProvider.validateToken(token)){
      Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //필터체인. 여기까지 오지 않는다면 다음체인으로 가지 않음.
    filterChain.doFilter(request, response);
  }
}