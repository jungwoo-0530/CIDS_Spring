package com.jungwoo.apiserver;

import com.jungwoo.apiserver.domain.maria.Member;
import com.jungwoo.apiserver.serviece.MemberService;
import org.apache.tomcat.util.descriptor.web.XmlEncodingBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class ApiServerApplication {

  @PostConstruct
  void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
  public static void main(String[] args) {
    SpringApplication.run(ApiServerApplication.class, args);
  }


}
