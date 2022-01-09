# API 구축 과제 - 지원자 김영준

## 기술
- 과제 필수 요구사항인 java, spring-boot, jpa, h2, gradle을 구현에 사용하였습니다.
- 인증부는 spring security + jwt를 db 스키마 관리는 liquibase를 추가로 사용하였습니다.
- h2는 http://localhost:8080/h2-console 에서 id: young-jun / password:공란 으로 확인가능합니다.
- swagger-ui는 http://localhost:8080/swagger-ui/ 에서 확인 가능합니다.

## 1-1. 회윈가입 API
- path: /szs/signup
- param: userId(String), password(String), name(String), regNo(String)
- 패스워드는 BCryptPasswordEncoder를 사용하여 단방향 암호화 처리하였습니다.
- 주민등록번호는 AES256알고리즘을 사용하여 양방향 암호화 처리하였습니다.
- 주민등록번호는 정규성검증하여 정해진 패턴만 입릭가능하도록 처리하였습니다.

## 1-2 로그인 API
- path: /szs/login
- param: userId(String), password(String)
- 인증에는 스프링 시큐리티를 사용하였고 jwt filter를 추가하였습니다.
- 로그인 성공시 jwt token을 발급하고 해당 토큰의 유효성을 signup, login을 제외한 모든 api경로에서 확인하도록 구성하였습니다.

## 1-3 내 정보보기 API
- path: /szs/me
- param: x
- 인증 토큰 이용하여 본인 정보만 확인하도록 구성하였습니다.

## 2 유저 정보 스크랩 API
- path: /szs/scrap
- param: x
- 인증 토큰을 이용하여 본인 정보만 확인하도록 구성하였습니다.
- 스크랩 정보를 저장하기 위해 tbl_scrap, tbl_scrap001, tbl_scrap002 테이블을 추가하였습니다.
- 로컬 database에 스크랩한 정보가 있으면 바로 응답, 아니면 스크랩 api를 호출하도록 구성하였습니다.
- 최초 화원 가입시 스크랩을 1회 진행하도록 구성하였습니다.
- 매일 새벽1시에 회원가입정보에는 존재하나 스크랩 정보가 없는 사용자를 대상으로\
  스크랩 정보를 호출 및 저장하도록 구성하였습니다.

## 3 환급액 계산 API
- path: /szs/refund
- param: x

## 주관식 과제