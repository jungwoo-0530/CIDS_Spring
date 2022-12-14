package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.mongo.CountDomain;
import com.jungwoo.apiserver.domain.mongo.Keyword;
import com.jungwoo.apiserver.domain.mongo.Result;
import com.jungwoo.apiserver.dto.mongo.detect.DetectDto;
import com.jungwoo.apiserver.dto.mongo.detect.DashboardDto;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.*;
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
  @Value("${email.text.success}")
  private String successText;

  @Value("${email.text.error}")
  private String errorText;



  @Value(("${python.flask.url}"))
  private String flaskBaseURL;

  private final JavaMailSender javaMailSender;
  private final KeywordRepository keywordRepository;
  private final ResultRepository resultRepository;

  private final CountDomainRepository countDomainRepository;

  private final CsvUtil csvUtil;

  private static final String FROM_ADDRESS = "admin@gmail.com";


  //1. keyword(String)??? ????????? ?????? ?????? ???????????? ???????????? ?????? ???????????????
  //2. ?????? ?????? ?????? ????????? ?????? ????????? ?????? save??? ?????? ?????? null??? ??????.
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

  @Async(value = "taskExecutor")
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

    //result = keyword collection??? _id?????? ?????????
    //ObjectId()??? ????????? ?????????.
    //RestTemplate Blocking

    try{
      String keywordId = restTemplate.getForObject(targetUrl, String.class);
      String resultFileName = makeCsv(keywordId, detectDto.getUserId(), detectDto.getKeyword());

      sendEmail(detectDto, resultFileName, successText);
    }catch (HttpClientErrorException e){//4xx
      sendErrorEmail(detectDto, errorText);
    }catch(HttpServerErrorException e){//5xx
      sendErrorEmail(detectDto, errorText);
    }
  }

  @Async(value = "taskExecutor")
  public void test(DetectDto detectDto) throws InterruptedException {

    String s = "hi";
    s.contains("hi");

    log.info("test Start");
    log.info("Thread {}", Thread.currentThread().getName());
    log.info("Sleep");
    Thread.sleep(50000);
    log.info("Sleep End");
  }

  public void sendEmail(DetectDto detectDto, String fileName, String text) throws MessagingException, UnsupportedEncodingException {

    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

    messageHelper.setFrom(FROM_ADDRESS);
    messageHelper.setTo(detectDto.getEmail());
    messageHelper.setSubject(subject);
    messageHelper.setText(text);

    String pathFile = "./searchModule/data_dir/send_csv_list/" + fileName;

    FileDataSource fds = new FileDataSource(pathFile);

    messageHelper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), fds);

    javaMailSender.send(message);

  }

  public void sendErrorEmail(DetectDto detectDto, String text) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

    messageHelper.setFrom(FROM_ADDRESS);
    messageHelper.setTo(detectDto.getEmail());
    messageHelper.setSubject(subject);
    messageHelper.setText(text);

    javaMailSender.send(message);
  }

  public String makeCsv(String keywordId, String userEmail, String keyword) throws IOException {
    List<Result> result = resultRepository.findAllByKeywordIdAndLabel(keywordId, 0);

    String title = userEmail + "_" + keyword;

    return csvUtil.writeCSV(title, result.stream().map(r -> r.getUrl()).collect(Collectors.toList()));
  }

  @Transactional
  public DashboardDto getDashboard(){


    List<Result> results = resultRepository.findAllByLabel(0);
    OptionalDouble average = results.stream().map(r -> r.getAccuracy()).mapToDouble(r -> r.doubleValue()).average();


    return DashboardDto.builder().searchKeywords(keywordRepository.getKeywordsOrderByDesc()).averageAccuracy(average.getAsDouble()).keywordCount(keywordRepository.countKeywordCount()).domains(countDomainRepository.getTop5DomainsOrderByHit()).memberCount(keywordRepository.getMemberAtKeywords()).build();

  }

  @Transactional
  public Page<CountDomain> findAllPage(Pageable pageable){
    return countDomainRepository.findCountDomainPaging(pageable);
  }

}