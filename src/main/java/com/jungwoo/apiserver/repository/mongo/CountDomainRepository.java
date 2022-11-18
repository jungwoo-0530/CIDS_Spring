package com.jungwoo.apiserver.repository.mongo;

import com.jungwoo.apiserver.domain.mongo.CountDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * fileName     : CountDomainRepository
 * author       : jungwoo
 * description  :
 */
@Repository
//@EnableMongoRepositories
public interface CountDomainRepository extends MongoRepository<CountDomain, String>, CountDomainRepositoryCustom {
}
