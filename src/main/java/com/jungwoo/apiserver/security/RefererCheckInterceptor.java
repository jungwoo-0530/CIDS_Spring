package com.jungwoo.apiserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * fileName     : ReferrerCheckInterceptor
 * author       : jungwoo
 * description  : CSRF 공격을 막기 위한 요청 HTTP Referer의 값과 Host의 값과 비교
 */
@Slf4j
public class RefererCheckInterceptor implements HandlerInterceptor {

  @Value("spring.front.host")
  private String frontHost;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


    String referer = request.getHeader("Referer");
    String host = request.getHeader("host");

    log.info("Request Uri : {}", request.getRequestURI());
    log.info("Request Url : {}", request.getRequestURL());
    log.info("referer : {}", referer);
    log.info("host : {}", host);
    for (Cookie cookie : request.getCookies()) {

      log.info("cookie : {}", cookie.getName());
      log.info("cookie : {}", cookie.getValue());
    }

    if (referer == null || !referer.contains(frontHost)) {
      log.info("CSRF 공격 위험, Referer가 없거나 Host가 포함되어있지 않습니다.");
      return false;
    }

    return true;
  }
}
