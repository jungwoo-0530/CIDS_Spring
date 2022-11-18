package com.jungwoo.apiserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * fileName     : AsyncConfig
 * author       : jungwoo
 * description  :
 */
@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2); // 기본적으로 실행을 대기하는 Thread의 갯수
    executor.setMaxPoolSize(10); // 동시 동작하는 최대 Thread 갯수
    executor.setQueueCapacity(500); // MaxPoolSize를 초과하는 요청이 Thread 생성 요청시 해당 내용을 Queue에 저장하게 되고, 사용할 수 있는 Thread 여유 자리가 발생하면 사나씩 꺼내져서 동작하게된다.
    executor.setThreadNamePrefix("search-async-");
    executor.initialize();
    return executor;
  }
}
