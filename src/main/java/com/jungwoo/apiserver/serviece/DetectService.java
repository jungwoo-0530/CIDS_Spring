package com.jungwoo.apiserver.serviece;

import com.jungwoo.apiserver.domain.mongo.CountDomain;
import com.jungwoo.apiserver.domain.mongo.Keyword;
import com.jungwoo.apiserver.domain.mongo.Result;
import com.jungwoo.apiserver.dto.mongo.detect.DetectDto;
import com.jungwoo.apiserver.dto.mongo.detect.DashboardDto;
import com.jungwoo.apiserver.dto.mongo.detect.DomainsDto;
import com.jungwoo.apiserver.dto.mongo.detect.SearchKeywordDto;
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


  @Transactional
  public DashboardDto getDashboard(){


    List<Result> results = resultRepository.findAllByLabel(0);
    OptionalDouble average = results.stream().map(r -> r.getAccuracy()).mapToDouble(r -> r.doubleValue()).average();

    List<Keyword> keywords = keywordRepository.findAll();
    List<SearchKeywordDto> searchKeywords = getSearchKeywordsSorted(keywords);

    int memberCount = getMemberCount(keywords);

    //domains
    List<CountDomain> countDomains = countDomainRepository.findTop5ByOrderByHitDesc();
//    List<String> domains = countDomains.stream().map(c -> c.getDomain()).collect(Collectors.toList());
    List<DomainsDto> domainsDtos = getDomainsDto(countDomains);

    return DashboardDto.builder().searchKeywords(searchKeywords).averageAccuracy(average.getAsDouble()).keywordCount(keywordRepository.count()).domains(domainsDtos).memberCount((long) memberCount).build();

  }

  private List<SearchKeywordDto> getSearchKeywordsSorted(List<Keyword> keywords){


    Map<String, Integer> temp = new HashMap<>();
    for(Keyword k : keywords){
      Integer count = temp.get(k.getKeyword());
      if(count == null){
        temp.put(k.getKeyword(), 1);
      }else{
        temp.put(k.getKeyword(), count+1);
      }
    }
    List<String> keySet = new ArrayList<>(temp.keySet());
    keySet.sort((o1, o2) -> temp.get(o2).compareTo(temp.get(o1)));

    List<String> subList = new ArrayList<>(keySet.subList(0, 5));

    List<SearchKeywordDto> result = new ArrayList<>();
    Long l = 1L;
    for(String s : subList){
      result.add(SearchKeywordDto.builder().rank(l).keyword(s).build());
      l++;
    }

    return result;
  }

  private List<DomainsDto> getDomainsDto(List<CountDomain> domains){

    Long l = 1L;
    List<DomainsDto> result = new ArrayList<>();
    for(CountDomain d : domains){
      result.add(DomainsDto.builder().rank(l).domain(d.getDomain()).build());
      l++;
    }
    return result;
  }

  private int getMemberCount(List<Keyword> keywords){


    Map<String, Integer> temp = new HashMap<>();
    for(Keyword k : keywords){
      Integer count = temp.get(k.getKeyword());
      if(count == null){
        temp.put(k.getKeyword(), 1);
      }
    }


    return temp.size();
  }

}