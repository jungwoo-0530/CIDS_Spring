package com.jungwoo.apiserver.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
/**
 * fileName     : JwtAuthenticationEntryPoint
 * author       : jungwoo
 * description  : (비로그인)상태, 인증에 실패한 사용자의 response에 HttpServletResponse.SC_UNAUTHORIZED를 담아줌, 401 에러
 *                즉, 토큰 인증 자체가 안되는 경우.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

  private static final long serialVersionUID = -7858869558953243875L;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException {

//    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 인증 실패");
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
  }
}