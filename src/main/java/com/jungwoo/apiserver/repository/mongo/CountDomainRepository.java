package com.jungwoo.apiserver.repository.mongo;

import com.jungwoo.apiserver.domain.mongo.CountDomain;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * fileName     : CountDomainRepository
 * author       : jungwoo
 * description  :
 */
@Repository
//@EnableMongoRepositories
public interface CountDomainRepository extends MongoRepository<CountDomain, String>, CountDomainRepositoryCustom {
  List<CountDomain> findTop5ByOrderByHitDesc();

}
