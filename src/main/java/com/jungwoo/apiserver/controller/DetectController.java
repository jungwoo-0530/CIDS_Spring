package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.BasicResponse;
import com.jungwoo.apiserver.dto.CommonResponse;
import com.jungwoo.apiserver.dto.mongo.detect.DashboardDto;
import com.jungwoo.apiserver.dto.mongo.detect.DetectDto;
import com.jungwoo.apiserver.serviece.DetectService;
import com.jungwoo.apiserver.serviece.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * fileName     : DetectController
 * author       : jungwoo
 * description  :
 */

@Api(tags = "CIDS 메인 서비스 Controller")
@RestController
@RequiredArgsConstructor
@Slf4j
public class DetectController {

  private final DetectService detectService;
  private final MemberService memberService;


  @ApiOperation(value = "CIDS 메인 서비스")
  @PostMapping("/detect")
  public ResponseEntity<? extends BasicResponse> searchService(@Validated @RequestBody DetectDto detectDto,
                                                               HttpServletRequest request) throws IOException, MessagingException {

    Member member = memberService.getMemberByRequestJwt(request);

    String keywordId = detectService.save(detectDto.getKeyword(), member.getLoginId());


    detectService.execDetect(DetectDto.builder()
        .keyword(detectDto.getKeyword())
        .email(member.getEmail())
        .userId(member.getLoginId())
        .build(), keywordId);


    return ResponseEntity.status(200).body(new CommonResponse<>("detect 완료"));
  }

  @ApiOperation(value = "대쉬보드")
  @GetMapping("/dashboard")
  public ResponseEntity<? extends BasicResponse> dashboard() {

    DashboardDto dto = detectService.getDashboard();

    return ResponseEntity.status(200).body(new CommonResponse<>(dto, "dashboard 완료"));
  }

  @PostMapping("/detectTest")
  public ResponseEntity<? extends BasicResponse> testSearchService(@RequestBody DetectDto detectDto,
                                                                   HttpServletRequest request) throws InterruptedException {

    Thread.sleep(10000);

    detectService.test(detectDto);
    return ResponseEntity.status(200).body(new CommonResponse<>(detectDto, "detect 완료"));
  }
}
