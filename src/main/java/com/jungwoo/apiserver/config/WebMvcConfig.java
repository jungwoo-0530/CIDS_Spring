package com.jungwoo.apiserver.config;

import com.jungwoo.apiserver.security.RefererCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * fileName     : WebMvcConfig
 * author       : jungwoo
 * description  : Web Mvc 설정 파일.
 */

@EnableAsync
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



  //비동기를 위한 백그라운드 쓰레드 풀 설정.
  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
    te.setCorePoolSize(10);
    te.setMaxPoolSize(100);
    te.setQueueCapacity(50);
    te.setThreadNamePrefix("Async Thread");
    te.setKeepAliveSeconds(600);
    te.initialize();

    return te;
  }
}
