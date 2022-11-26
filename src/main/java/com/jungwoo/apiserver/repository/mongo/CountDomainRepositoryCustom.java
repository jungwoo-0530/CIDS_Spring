package com.jungwoo.apiserver.repository.mongo;


import com.jungwoo.apiserver.domain.mongo.CountDomain;
import com.jungwoo.apiserver.dto.mongo.detect.DomainsDto;

import java.util.List;

/**
 * fileName     : CountDomainRepositoryCustom
 * author       : jungwoo
 * description  :
 */
public interface CountDomainRepositoryCustom {

  List<DomainsDto> getTop5DomainsOrderByHit();
}
