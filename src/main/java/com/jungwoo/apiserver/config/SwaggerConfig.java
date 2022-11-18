package com.jungwoo.apiserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * fileName     : SwaggerConfig
 * author       : jungwoo
 * description  :
 */
@Configuration
public class SwaggerConfig {

  private static final String API_NAME = "CIDS Project API";
  private static final String API_VERSION = "0.0.1";
  private static final String API_DESCRIPTION = "CIDS 프로젝트 명세서";

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.OAS_30)
        .useDefaultResponseMessages(false)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.jungwoo.apiserver.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title(API_NAME)
        .description(API_DESCRIPTION)
        .version(API_VERSION)
        .build();
  }
}