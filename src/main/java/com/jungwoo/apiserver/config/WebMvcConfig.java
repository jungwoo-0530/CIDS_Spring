package com.jungwoo.apiserver.config;

import lombok.RequiredArgsConstructor;
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


  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .exposedHeaders("Authorization")
        .exposedHeaders("Set-Cookie")
        .allowCredentials(true)
        .allowedOrigins("http://localhost:3000")
        .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE"); }

}
