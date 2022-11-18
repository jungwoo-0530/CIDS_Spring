package com.jungwoo.apiserver.security.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * fileName     : JwtAccessDeniedHandler
 * author       : jungwoo
 * description  : 접근 권한이 없이 접근하려 할 때, 403에러 발생.
 *                 즉, 토큰이 정상적으로 왔지만 해당 자원에 대한 접근 권한이 없는 경우.
 */

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

    response.sendError(HttpServletResponse.SC_FORBIDDEN, "해당 자원에 대한 접근 권한이 없습니다.");
  }
}