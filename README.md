# 요구사항

## Application 기술 스펙

java, spring-boot, jpa, h2, gradle

## 기능명세
1. 사용자가 삼쩜삼에 가입
2. 가입한 유저정보를 스크랩하여 환급액이 있는지 조회
3. 조회된 금액을 계산 후 사용자에게 실제 환급액 전달

## API

### 회윈가입
- path: /szs/signup
- param: userId(String), password(String), name(String), regNo(String)

### 로그인
- path: /szs/login
- param: userId(String), password(String)

### 사용자 정보 조회
- path: /szs/me

### 사용자 정보 스크랩
- path: /szs/scrap

### 환급액 계산
- path: /szs/refund

## 제약
- 응답은 모두 application/json으로
- 각 기능 및 제약사항에 대한 단위 테스트
- swagger
- 민감정보(주민등록번호, 비밀번호)등 암호화
- README.md 작성 (요구사항 구현 여부, 구현 방법, 검증 결과)