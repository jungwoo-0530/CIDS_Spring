package com.jungwoo.apiserver.controller;

import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.dto.BasicResponse;
import com.jungwoo.apiserver.dto.CommonResponse;
import com.jungwoo.apiserver.dto.mongo.detect.DetectDto;
import com.jungwoo.apiserver.serviece.DetectService;
import com.jungwoo.apiserver.serviece.MemberService;
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

@RestController
@RequiredArgsConstructor
@Slf4j
public class DetectController {

  private final DetectService detectService;
  private final MemberService memberService;


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


}
