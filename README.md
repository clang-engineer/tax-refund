# API 구축 과제 - 김영준

# API 정리
## 개요
- 과제 필수 요구사항인 java, spring-boot, jpa, h2, gradle을 구현에 사용하였습니다.
- 인증부는 spring security + jwt를 db 스키마 관리는 liquibase를 추가로 사용하였습니다.
- h2는 http://localhost:8080/h2-console 에서 id: young-jun / password:공란 으로 확인가능합니다.
- swagger-ui는 http://localhost:8080/swagger-ui/ 에서 확인 가능합니다.

## 1-1. 회윈가입 API
- path: /szs/signup
- param: userId(String), password(String), name(String), regNo(String)
- 패스워드는 BCryptPasswordEncoder를 사용하여 단방향 암호화 처리하였습니다.
- 주민등록번호는 AES256알고리즘을 사용하여 양방향 암호화 처리하였습니다.
- 주민등록번호는 정규성검증하여 정해진 패턴만 입력가능하도록 처리하였습니다.
- 회원가입에 필요한 4가지 파라미터는 모두 필수 입력값으로 제약을 추가했습니다.
- 패스워드는 4자리 이상 60자리만 입력되록만 제약을 뒀습니다. (ex> 1234(o), abcd(o), 123(x))

## 1-2. 로그인 API
- path: /szs/login
- param: userId(String), password(String)
- 인증에는 스프링 시큐리티를 사용하였고 jwt filter를 추가하였습니다.
- 로그인 성공시 jwt token을 발급하고 해당 토큰의 유효성을 signup, login을 제외한 모든 api경로에서 확인하도록 구성하였습니다.

## 1-3. 내 정보보기 API
- path: /szs/me
- param: x
- 인증 토큰을 이용하여 로그인한 본인 정보만 확인하도록 구성하였습니다.

## 2. 유저 정보 스크랩 API
- path: /szs/scrap
- param: x
- 인증 토큰을 이용하여 로그인한 본인 정보만 확인하도록 구성하였습니다.
- 스크랩 정보를 저장하기 위해 tbl_scrap, tbl_scrap_salary(scrap001), tbl_scrap_tax(scrap002) 로 테이블을 구성하였습니다.
- JPA 연관 관계 ScrapSalary to Scrap(N:1), ScrapTax to Scrap(N:1)를 각각 형성하였습니다.
- database에 스크랩한 정보가 있으면 바로 응답, 아니면 스크랩 api를 호출하여 응답하도록 구성하였습니다.
- 최초 회원 가입시 스크랩URL을 별도 스레드에서 1회 호출하도록 구성하였습니다.
- 매일 새벽1시에 회원가입정보에는 존재하나 로컬DB에 스크랩 정보가 없는 사용자를 대상으로 스크랩URL 호출 및 저장하도록 구성하였습니다.

## 3. 환급액 계산 API
- path: /szs/refund
- param: x
- 인증 토큰을 이용하여 본인 정보만 확인하도록 구성하였습니다.
- database에 스크랩한 정보가 있으면 바로 환급액 계산 결과를 응답, 아니면 스크랩 api를 호출 >> 환급액 계산하여 응답하도록 구성하였습니다.
- 환급액 계산을 위해 필요한 정보(총급여액, 산출세액)가 불충분한 경우는 예외로 처리하도록 하였습니다.


# 검증 결과
## 1.Test Report
총 42개의 단위 테스트를 통과했고 실행시간은 5.192초가 걸렸스닙다. 외부 연동이 필요한 테스트들은 대역을 사용해 의존성을 없앴습니다.
![Screen Shot 2022-01-13 at 7 46 16 PM](https://user-images.githubusercontent.com/39648594/149315952-4afc660c-5db2-4da4-81d4-2bbcf5381b08.png)

## 2. Code Coverage
domain, dto, static util을 제외한 전체 코드의 93%를 커버하는 단위테스트를 작성하였습니다.
![Screen Shot 2022-01-13 at 7 43 14 PM](https://user-images.githubusercontent.com/39648594/149315578-6648c8de-7f0c-43fc-8a48-4e343c5b142b.png)

# 주관식 과제

1. 테스트코드 작성시 setup 해야 할 데이터가 대용량이라면 어떤 방식으로 테스트코드를 작성하실지 서술해 주세요.

- 대용량 데이터 연동부를 대역으로 대처하는 테스트 코드를 작성하겠습니다. 
- 스텁(or모의) 객체를 사용해서 데이터 호출결과에 대한 응답값을 대체 하도록 하겠습니다.
- 경우에 따라 스파이(or모의) 객체를 사용해서 결과값에 대한 테스트가 아닌 행위에 대한 테스트를 구성하도록 하겠습니다.

2. 이벤트 드리븐 기반으로 서비스를 만들 때 이벤트를 구성하는 방식과 실패 처리하는 방식에 대해 서술해 주세요.
- 로컬 트랜잭션을 이용하는 방법: 특정 테이블의 레코드가 갱신될 때 이벤트 테이블에 해당 이벤트 정보를 기록하는 방식. 기록된 이벤트 정보는 이벤트 퍼블리셔에 의해서 메시지 브로커에게 방행되며 발행 된 이벤트는 다시 이벤트 테이블에 상태가 갱신됨. 로컬 트랜잭션의 ACID특성으로 인해서, 레코드가 갱신되는 경우 반드시 이벤트가 발행되는 것을 보장할 수 있음. 레코드 갱신 시 이벤트 테이블에 관련 정보를 추가해야 한다는 점을 개발자가 기억하고 있어야 하기 때문에 실수할 가능성이 매우 높음.
- 트랜잭션 로그를 이용하는 방법: 트랜잭션 로그 마이너가 데이터 베이스 트랜잭션 로그의 변경을 감지하여 이벤트를 발행하는 방식. 비지니스 로직 내부에 이벤트 발행을 위한 별도의 처리가 필요하지 않기 때문에 애플리케이션 구조를 단순하게 유지할 수 있는 장점이 있음. 허나, 데이터베이스의 특성에 따라 로그의 형식이 각기 다를 수 있으므로 해당 로그의 내용만으로 비지니스 이벤트를 추출해야하는 어려움이 있음
- 이벤트 소싱: 순수하게 이벤트에만 의존하여 원자성을 보장하는 방식. 이벤트 스토어라는 저장소를 통해 객체의 상태변화에 대한 이벤트를 저장하거나 조회할 수 있는 API를 제공한다. 이를 통해 이벤트 스토어가 메시지 브로커의 역할을 대체함. 도메인 모델이 복잡해질수록 변경에 대한 유연한 대처가 가능하다는 장점이 있지만 이벤트의 수가 많아질수록 성능이 매우 느려질 수 있다는 단점이 있음. + 개발자에게 별도의 학습 기간이 필요함
 
3. MSA 구성하는 방식에는 어떤 것들이 있고, 그중 선택하신다면 어떤 방식을 선택하실 건가요?
- API Gateway패턴: 서비스로 전달되는 모든 API요청이 통과하는 관문 역활 서버를 배치하는 패턴. 시스템의 아키텍처를 내부로 숨기고 외부의 요청에 대한 응답만을 적절한 형태로 응답.
- BFF(Backend For Frontend) 패턴: API 게이트웨이와 같은 진입점을 하나로 두지 않고 프런트엔드의 유형에 따라 각각 두는 패턴.
- Service Discovery: 클라우드 환경에서 autoscaling 생성, 삭제, 확장 과정에서 동적으로 변경되는 인스턴스의 IP,PORT를 쉽게 찾기 위한 패턴.
- Sidecar pattern: 어플리케이션 컨테이너와 독립적으로 동작하는 별도의 컨테이너를 구성하고 이를 어플리케이션에 적용하는 패턴. 어플리케이션의 핵심 로직과 주변 로직을 분리할 수 있는 장점이 있지만, 어플리케이션의 크기가 작은 경우 불필요한 부하를 줄수 있다.

- 선택? 비교적 구현이 간단해 보이는 API Gateway패턴을 선택해 서비스 단일 진입점과 아키텍처를 구성하고, 추후에 서비스 규모에 따라 필요한 패턴과 설계를 추가하도록 하겠습니다.

4. 외부 의존성이 높은 서비스를 만들 때 고려해야 할 사항이 무엇인지 서술해 주세요.
- 라이브러리에 대한 학습테스트들을 구성해서 버전 변경이 발생하더라도 기능들이 동일하게 작종 하는지 확인 할 수 있어야 합니다. 
- 라이센스, 구매 비용, 상업 배포 가능 여부, 추후유지보수 지원 등을 사전에 확인해야합니다. 
 
5. 일정이 촉박한 프로젝트를 진행하게 되었습니다. 이 경우 본인의 평소 습관에 맞춰 개발을 진행할 지, 회사의 코드 컨벤션에 맞춰 개발할지 선택해 주세요. 그리고 그 이유를 서술해 주세요.
- 코드 컨벤션에 맞춰서 개발 하겠습니다. 여러명이 동시에 프로젝트를 진행한다는 전제하에 특정 기준없이 각자 스타일대로 개발하면 향후 유지보수성이 낮아질 것이기 때문 입니다. 
또 급한 상황에서 컨벤션을 갑자기 바꾼다면 예상치 못한 부분에서 기존에 유지하고 있던 알고리즘들이 깨질 수 있기 때문입니다.
 
6. 민감정보 암호화 알고리즘에는 어떤 것들이 있고, 그중 선택하신다면 어떤 것을 선택하실 건가 요? 그 이유는 무엇인가요?
- 암복호화가 가능하고 속도가 우수한 대칭키 알고리즘을 선택하겠습니다. 한국인터넷 진흥원(kisa)에서 권고하는 안전한 대칭키 알고리즘 AES, SEED, HIGHT, ARIA, LEA 등에서 택1 할 수 있는데, 
소프트웨어로 구현이 용이한 AES를 선택하도록 하겠습니다.
> 참조: [암호 알고리즘 및 키 길이 이용 안내서(2018.12)])(https://seed.kisa.or.kr/kisa/Board/38/detailView.do)

