# CIDS 백엔드 - Spring Boot

최근 5년간 영화 드라마, 웹툰 등의 콘텐츠 저작권 불법 침해 사이트의 수가 2만 1043개에 달하는것으로 나타났다.

현재는 저작권 침해 사이트 탐지를 위한 서비스가 보편화 되어있지 않다. 따라서 어떻게 하면 사용자들이 편리하게 접근하여 저작권 침해 의심 사이트를 탐지할 수 있을까 생각하였고 이러한 프로젝트를 기획하게 되었다.

저작권 침해 사이트들은 대부분 불법 광고(배너)를 통하여 수익을 창출하기 때문에 Google Vision API에 있는 OCR API를 사용하여 배너들이 불법 광고 배너인지 판단(머신 러닝)하여 해당 사이트가 저작권 침해 의심 사이트인지 알 수 있음.





# 목차

- [주요 기능과 로직](#주요-기능과-로직)

- [서비스 구조](#서비스-구조)
- [기술 스택](#기술-스택)
- [기획 & 설계](#기획-&-설계)
  - [기능 명세서](#기능-명세서)
  - [페이지 기획서](#페이지-기획서)
  - [API 명세서 Spring Boot](#API-명세서-Spring-Boot)
  - [DB 명세서](#DB-명세서)

- [Front & 머신 러닝(Flask)](Front-&-머신-러닝(Flask))



## 주요 기능과 로직

- 저작권 침해 사이트 검색 서비스(CIDS 서비스) : **웹툰 저작권 침해 의심되는 키워드**를 입력하면 저작권 침해 의심되는 사이트를 **이메일**로 보내줌
- 게시판 기능 : 공지사항, QnA 게시판에서 운영자, 사용자가 글을 올리고 이미지도 올릴 수 있음
- 댓글 기능 : 중첩 세트 모델(The Nested Set Model)을 사용하여 대댓글 기능을 구현
- 페이징 기능 : 게시물, 대쉬 보드, 의심 도메인, 회원들 카테고리를 백에서 프론트로 알맞는 개수씩 보내줌
- 로그인 : 로그인시 백엔드에서 유저에게 JWT토큰을 발급하여 인증이 필요한 요청에서 JWT토큰을 이용하여 사용자 인증
- 마이 페이지 : 마이페이지에서 개인 정보 변경과 이미지를 설정할 수 있음
- 크롤링 : 파이썬을 사용하여 유저가 CIDS 서비스에서 키워드를 입력시 그에 맞는 정보를 크롤링
- Multiple Database : 메인 DB(MariaDB)와 머신러닝 DB(MongoDB)인 두 개의 DB를 사용함
- 배포 : AWS EC2에 배포(jar파일로 빌드)



**주요 기능과 로직은 기능 명세서에 작성**

### CIDS 서비스

1. 사용자가 저작권 침해 의심 키워드 입력(프론트에서 백(Spring Boot)로 요청)

2. Spring Boot에서 Flask(Python)으로 해당 키워드를 요청
3. 해당 키워드를 가지고 구글 검색엔진을 통해 크롤링
4. 크롤링한 URL들의 들어있는 배너(광고)들의 문자열을 가지고 그 배너가 불법 광고에 쓰이는 배너인지 



## 서비스 구조

- 메인 서비스 플로우

<img src="img/README/image-20221126211733941.png" alt="image-20221126211733941" style="zoom:50%;" />





- CIDS Web App Architecture

<img src="img/README/Screenshot of Safari (2022-11-26 10-58-14 PM).png" alt="Screenshot of Safari (2022-11-26 10-58-14 PM)" style="zoom:50%;" />

- 이메일 결과

<img src="img/README/Screenshot of Mail (2022-11-26 9-16-15 PM).png" alt="Screenshot of Mail (2022-11-26 9-16-15 PM)" style="zoom: 33%;" />



## 기술 스택

- Front
  - Javascript, React
  - AWS EC2
- Back
  - Java - version 8, SpringBoot, Spring Data JPA, QueryDsl, Gradle
  - Python - version 3.10,  Flask, Google Vision Api
  - MariaDB, MongoDB
  - AWS(EC2, RDS, S3,  Document DB)

## 기획 & 설계

- [기능 명세서](https://github.com/jungwoo-0530/Study/blob/main/Spring/CIDS_Docs/기능%20명세서.md)
- [페이지 기획서](#페이지-기획서)
- [API 명세서](#API-명세서(Spring-Boot))

- [DB 명세서](#DB-명세서)



### 기능 명세서

### 페이지 기획서

### 

### API 명세서 Spring Boot

- Swagger 사용

<img src="img/README/Screenshot of Safari (2022-11-26 8-41-52 PM).png" alt="Screenshot of Safari (2022-11-26 8-30-07 PM)" style="zoom:50%;" />

<img src="img/README/Screenshot of Safari (2022-11-26 8-30-07 PM).png" alt="Screenshot of Safari (2022-11-26 8-30-07 PM)" style="zoom:50%;" />

<img src="img/README/Screenshot of Typora (2022-11-26 8-30-40 PM).png" alt="Screenshot of Typora (2022-11-26 8-30-40 PM)" style="zoom:50%;" />

<img src="img/README/Screenshot of Safari (2022-11-26 8-30-59 PM).png" alt="Screenshot of Safari (2022-11-26 8-30-59 PM)" style="zoom:50%;" />

<img src="img/README/Screenshot of Typora (2022-11-26 8-31-13 PM).png" alt="Screenshot of Typora (2022-11-26 8-31-13 PM)" style="zoom:50%;" />



### DB 명세서



- Maria DB

<img src="img/README/Screenshot of IntelliJ IDEA (2022-11-27 1-58-56 AM).png" alt="Screenshot of IntelliJ IDEA (2022-11-27 1-58-56 AM)" style="zoom:50%;" />

- Mongo DB

<img src="img/README/Screenshot of IntelliJ IDEA (2022-11-27 1-51-10 AM).png" alt="Screenshot of IntelliJ IDEA (2022-11-27 1-51-10 AM)" style="zoom:50%;" />

## Front & 머신 러닝(Flask)

- [FrontEnd](https://github.com/jungwoo-0530/CIDS_Front)
- [머신 러닝(Flask)](https://github.com/jungwoo-0530/CIDS_LM)
