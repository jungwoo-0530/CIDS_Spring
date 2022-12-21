# CIDS 백엔드 - Spring Boot

최근 5년간 영화 드라마, 웹툰 등의 콘텐츠 저작권 불법 침해 사이트의 수가 2만 1043개에 달하는것으로 나타났다.

현재는 저작권 침해 사이트 탐지를 위한 서비스가 보편화 되어있지 않다. 따라서 어떻게 하면 사용자들이 편리하게 접근하여 저작권 침해 의심 사이트를 탐지할 수 있을까 생각하였고 이러한 프로젝트를 기획하게 되었다.

저작권 침해 사이트들은 대부분 불법 광고(배너)를 통하여 수익을 창출하기 때문에 Google Vision API에 있는 OCR API를 사용하여 배너들이 불법 광고 배너인지 판단(머신 러닝)하여 해당 사이트가 저작권 침해 의심 사이트인지 알 수 있음.



- 참고로 이 프로젝트에는 모든 커밋이 존재하지 않습니다.
- 배포하기 위해서 프론트엔드, 백엔드같이 있던 프로젝트를 분리했습니다.
- [이전 프로젝트 링크](https://github.com/jungwoo-0530/SpringBootApiServer)



# 목차

- [주요 기능과 로직](#주요-기능과-로직)
- [서비스 구조](#서비스-구조)
- [기술 스택](#기술-스택)
- [기획 & 설계](#기획-설계)
  - [기능 명세서](#기능-명세서)
  - [API 명세서](#API-명세서)
  - [DB 명세서](#DB-명세서)
- [FrontEnd(React) & 머신 러닝(Flask)](#FrontEndReact머신-러닝Flask) 



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
  - Java - version 8, SpringBoot, Spring Data JPA, QueryDsl, Spring Security, Gradle
  - Python - version 3.10,  Flask, Google Vision Api
  - MariaDB, MongoDB
  - AWS(EC2, RDS, S3,  Document DB)

## 기획&설계

- [요구사항](#요구사항)
- [기능 명세서](https://github.com/jungwoo-0530/CIDS_Spring/blob/main/docs/기능%20명세서.md)
- [API 명세서](#API-명세서)
- [DB 명세서](#DB-명세서)

### 요구사항

1. 회원 가입 페이지
   1. 아이디는 영어 소문자, 대문자만 가능하고 최소 4, 최대 15글자로 제한하고 중복체크를 해야함.
   2. 비밀번호는 최소 8, 최대 16글자로 제한.
   3. 이메일은 이메일 형식에 맞춰서 작성해야하고 중복체크를 해야함.
   4. 핸드폰 번호는 핸드폰 패턴 xxx-xxxx-xxxx에 맞춰서 작성해야함.
2. 로그인
   1. 로그인을 하지 않은 경우 아래 페이지만 사용 가능
      1. 회원가입 페이지
      2. 로그인 페이지
      3. 게시글 목록 조회 페이지
   2. 아이디와 비밀번호 맞는 지 체크하고 틀릴 시 아이디가 틀렸는지 비밀번호가 틀렸는지 알려야함.
   3. 로그인 성공시 홈페이지(/)로 리다이렉트함.
3. 회원 정보 수정
   1. 회원 이미지 업로드시 적정 크기에 맞춰서 리사이즈해서 업로드해야함.
   2. 비밀번호 변경시 최소 8, 최대 16글자로 제한.
   3. 권한은 관리자만이 변경할 수 있어야함.
4. 게시글
   1. 로그인한 상태에서만 글을 쓸 수 있음
   2. 게시글 작성, 수정 시 제목과 내용은 빈칸으로 작성 못함
   3. 게시판 타입에 따라서 권한에 따른 게시글 작성
   4. 글 수정, 삭제는 글쓴이와 운영자만 가능
   5. 게시글 검색 시 제목, 제목&내용, 작성자 타입에 맞춰서 검색할 수 있어야함.
5. 댓글
   1. 로그인한 상태에서만 댓글 작성 가능
   2. 댓글 작성시 빈칸으로 작성 못함
   3. 댓글 삭제, 수정은 글쓴이와 운영자만 가능
   4. 계층형 댓글로 댓글 구성
6. CIDS 서비스
   1. 키워드 입력은 공백이 아닐 경우에만 입력이 가능함.
   2. CIDS 서비스는 회원인 사용자만 이용이 가능함.
   3. 사용자에게 이메일로 전송이 된다고 알려줘야함.
   4. 이메일에는 검출된 도메인 리스트를 받을 수 있어야하고 신고하기 편하게 링크도 보내줘야한다.
7. 대시보드, 탐색된 도메인 탐지 순위
   1. 대시보드는 총 키워드 검색 수, CIDS를 이용한 사용자 수, 머신 러닝 정확도가 보여줘야함.
   2. 검색 키워드 Top 5, 검출된 도메인 Top 5을 보여줘야함.

### 기능 명세서

- [링크](https://github.com/jungwoo-0530/CIDS_Spring/blob/main/docs/기능%20명세서.md)

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

## FrontEnd(React)&머신 러닝(Flask)

- [FrontEnd](https://github.com/jungwoo-0530/CIDS_Front)
- [머신 러닝(Flask)](https://github.com/jungwoo-0530/CIDS_ML)
