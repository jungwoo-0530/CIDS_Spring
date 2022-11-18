package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.mongo.CountDomain;
import com.jungwoo.apiserver.domain.mongo.Keyword;
import com.jungwoo.apiserver.domain.mongo.Result;
import com.jungwoo.apiserver.dto.mongo.countdomain.CountDomainPageDto;
import com.jungwoo.apiserver.dto.mongo.detect.DetectDto;
import com.jungwoo.apiserver.repository.mongo.CountDomainRepository;
import com.jungwoo.apiserver.repository.mongo.KeywordRepository;
import com.jungwoo.apiserver.repository.mongo.ResultRepository;
import com.jungwoo.apiserver.util.csv.CsvUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * fileName     : DetectService
 * author       : jungwoo
 * description  :
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class DetectService {
  @Value(("${email.subject}"))
  private String subject;
  @Value("${email.text}")
  private String text;

  @Value(("${python.flask.url}"))
  private String flaskBaseURL;

  private final JavaMailSender javaMailSender;
  private final KeywordRepository keywordRepository;
  private final ResultRepository resultRepository;

  private final CountDomainRepository countDomainRepository;

  private final CsvUtil csvUtil;

  private static final String FROM_ADDRESS = "admin@gmail.com";


  //1. keyword(String)을 받아서 또는 현재 진행중인 단어이면 바로 리턴해주는
  //2. 현재 진행 중인 단어는 중복 방지를 위해 save를 하지 않고 null을 반환.
  @Transactional
  public String save(String keyword, String loginId) {
    Keyword build = Keyword.builder()
        .keyword(keyword)
        .userId(loginId)
        .status(1)
        .updateDate(Date.from(ZonedDateTime.now().toInstant()))
        .createDate(Date.from(ZonedDateTime.now().toInstant()))
        .build();
    Keyword save = keywordRepository.save(build);
    return save.getId().toHexString();
  }

  @Async
  @Transactional
  public void execDetect(DetectDto detectDto, String id) throws IOException, MessagingException {

    RestTemplate restTemplate = new RestTemplate();

    URI targetUrl = UriComponentsBuilder
        .fromUriString(flaskBaseURL)
        .path("/api/detect")
        .queryParam("keyword", id)
        .build()
        .encode()
        .toUri();

    //result = keyword collection의 _id값이 넘어옴
    //ObjectId()가 제거된 형태로.
    String keywordId = restTemplate.getForObject(targetUrl, String.class);

    String resultFileName = makeCsv(keywordId, detectDto.getUserId(), detectDto.getKeyword());

    sendEmail(detectDto, resultFileName);

  }

  public void sendEmail(DetectDto detectDto, String fileName) throws MessagingException, UnsupportedEncodingException {

    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

    messageHelper.setFrom(FROM_ADDRESS);
//    messageHelper.setTo(detectDto.getEmail());
    messageHelper.setTo(detectDto.getEmail());
    messageHelper.setSubject(subject);
    messageHelper.setText(text);

    String pathFile = "./searchModule/data_dir/send_csv_list/" + fileName;

    FileDataSource fds = new FileDataSource(pathFile);

    messageHelper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), fds);

    javaMailSender.send(message);

  }



  public String makeCsv(String keywordId, String userEmail, String keyword) throws IOException {
    List<Result> result = resultRepository.findAllByKeywordIdAndLabel(keywordId, 0);

    String title = userEmail + "_" + keyword;

    return csvUtil.writeCSV(title, result.stream().map(r -> r.getUrl()).collect(Collectors.toList()));
  }


  @Transactional
  public Map<String, Object> findCountDomainPage(Pageable pageable) {
    Page<CountDomain> allPageSort = countDomainRepository.findAll(pageable);
    List<CountDomain> a = allPageSort.getContent();

    Map<String, Object> response = new HashMap<>();
    response.put("content", a);
    response.put("pageSize", pageable.getPageSize());
    response.put("currentPage", allPageSort.getNumber());
    response.put("totalItems", allPageSort.getTotalElements());
    response.put("totalPages", allPageSort.getTotalPages());
    response.put("numberOfElements", allPageSort.getContent().size());

    return response;
  }

}