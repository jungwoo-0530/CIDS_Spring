package com.jungwoo.apiserver.security.jwt;

import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

/**
 * fileName     : JwtAuthenticationProvider
 * author       : jungwoo
 * description  : JWT을 생성, 검증, 정보추출 해주는 클래스이다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider {

  private final UserDetailsService userDetailsService;

  @Value(("${security.jwt.token.secretKey}"))
  private String secretKey;
  @Value("${security.jwt.token.expire}")
  private Long tokenExpired;

  private static final String AUTHORIZATION = "Authorization";
  private static final String ACCESS_TOKEN_TYPE =
       JwtAuthenticationProvider.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";



  // JWT 토큰 생성
  public String createToken(String userPk) {
    Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims) // 정보 저장
        .setIssuedAt(now) // 토큰 발행 시간 정보
        .setExpiration(new Date(now.getTime() + tokenExpired)) // set Expire Time
        .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
        .compact();
  }

  public String getRole(String jwt) {
    Jws<Claims> claims = getClaimsJws(jwt);
    return (String) claims.getBody().get("roles");
  }

  //token에서 Claims를 얻음.
  private Jws<Claims> getClaimsJws(String jwt) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
  }

  // JWT 토큰에서 인증 정보 조회
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // 토큰의 만료기간 조회 분으로 환산 후.
  public Date getTokenExpired(String token){
//    return tokenExpired/60000;
    Jws<Claims> claims = getClaimsJws(token);

    return claims.getBody().getExpiration();
  }

  // 토큰에서 회원 정보 추출
  public String getUserPk(String token) {
    return getClaimsJws(token).getBody().getSubject();
  }

  // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
  public String getTokenInRequestHeader(HttpServletRequest request, String type) {
    Enumeration<String> headers = request.getHeaders(AUTHORIZATION);

    return getHeaderValue(headers, type);
  }
  //header 값들 중 type에 맞는 값을 가져온다.
  public String getHeaderValue(Enumeration<String> headers, String type){
    while(headers.hasMoreElements()){
      String value = headers.nextElement();
      if(value.toLowerCase().startsWith(type.toLowerCase())) {
        return value.substring(type.length()).trim();
      }
    }
    return Strings.EMPTY;
  }

  // 토큰의 유효성 + 만료일자 확인
  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims = getClaimsJws(jwtToken);
      log.info(String.valueOf(claims.getBody().getExpiration()));
      log.info(String.valueOf(new Date()));
      return !claims.getBody().getExpiration().before(new Date());
    } catch (SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }


}
