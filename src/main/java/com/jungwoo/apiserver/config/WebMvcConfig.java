package com.jungwoo.apiserver.config;

import com.jungwoo.apiserver.security.RefererCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * fileName     : WebMvcConfig
 * author       : jungwoo
 * description  : Web Mvc 설정 파일.
 */

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${spring.front.uri}")
  private String frontUri;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .exposedHeaders("Authorization")
        .exposedHeaders("Set-Cookie")
//        .allowCredentials(true)
        .allowCredentials(true)
        .allowedOrigins(frontUri)
        .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE"); }


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RefererCheckInterceptor())
        .addPathPatterns("/*");
  }
}
