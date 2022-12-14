package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.mongo.CountDomain;
import com.jungwoo.apiserver.serviece.DetectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * fileName     : CountDomainController
 * author       : jungwoo
 * description  :
 */

@Api(tags = "도메인 랭킹 API Controller")
@RestController
@RequiredArgsConstructor
public class CountDomainController {

  private final DetectService detectService;

  @ApiOperation(value = "도메인 랭킹을 조회.")
  @GetMapping("/domain/ranking")
  public Page<CountDomain> list(@PageableDefault(size = 4, sort = "hit",
      direction = Sort.Direction.DESC) Pageable pageable) {

    return detectService.findAllPage(pageable);
  }
}
