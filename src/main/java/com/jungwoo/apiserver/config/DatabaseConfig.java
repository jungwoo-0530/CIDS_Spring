package com.jungwoo.apiserver.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 * fileName     : QuerydslConfig
 * author       : jungwoo
 * description  :
 */
@EnableJpaAuditing
@Configuration
public class DatabaseConfig {


  //JPA
  @PersistenceContext
  private EntityManager entityManager;

  public DatabaseConfig() {
  }

  //JPAQueryFactory를 빈으로 등록함으로써 repository에서 바로 가져와서 사용.
  //JPAQueryFactory가 우리가 작성한 것을 토대로 entityManager를 통하여 질의.
  @Bean
  public JPAQueryFactory jpaQueryFactory(){
    return new JPAQueryFactory(entityManager);
  }

  @Value("${spring.data.mongodb.uri}")
  private String connectionString;

  public MongoDatabaseFactory mongoDatabaseFactory() {
    return new SimpleMongoClientDatabaseFactory(connectionString);
  }

  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongoDatabaseFactory());
  }

}
