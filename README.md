# SmsMonitor (SMS 기반 소비 관리 서비스)

---
## 프로젝트 개요
사용자의 SMS(카드 결제 문자)를 파싱하여 **지출 내역을 자동으로 관리하고 분석**하는 API 서비스입니다.
사용자는 결제 문자를 전송하여 지출 내역을 등록하고, 특정 조건(고액 결제, 심야 결제 등)에 따른 **'멍청비용' 분석**을 제공받습니다.
또한 관리되는 지출 데이터를 **구글 스프레드시트**로 내보내어 외부에서 관리할 수 있는 기능을 제공합니다.

---
## 기술 스택
* **Language**: Java 17
* **Framework**: Spring Boot 3.4.5
* **Persistence**: Spring Data JPA, MySQL
* **Security**: Spring Security, JWT
* **API Doc**: SpringDoc OpenAPI (Swagger), Spring RestDocs
* **External API**: Google Sheets API v4
* **Test**: JUnit5, Mockito

---
## 핵심 기능
1. **사용자 및 인증 (User Domain)**
    * **회원가입 및 로그인**: JWT 기반 인증 시스템 (Access/Refresh Token).
    * **사용자 승인 시스템**: 관리자 승인(`isApproved`)을 받은 사용자만 서비스 이용 가능.
2. **거래 내역 관리 (Transaction Domain)**
    * **SMS 자동 파싱**: KB국민카드, NH농협카드 등 다양한 SMS 포맷 지원 및 자동 지출 등록.
    * **지출 분석 (Stupid Cost Analysis)**: 
        * 고액 결제(High Amount) 및 심야 시간대 결제(Late Night) 분석 전략 패턴 적용.
    * **메모 관리**: 등록된 지출 내역에 사용자 메모 추가 및 수정.
    * **데이터 내보내기 (Exporter)**: 지출 내역을 구글 스프레드시트로 전송.
3. **카테고리 관리 (Category Domain)**
    * 지출 항목별 카테고리 생성, 수정, 삭제 기능.
    * 멍청비용 대상 카테고리 설정 기능.

---
## 프로젝트 구조 (DDD - Domain Driven Design)
이 프로젝트는 도메인 중심 설계(DDD)를 기반으로 **Bounded Context**별로 패키지가 분리되어 있습니다.

```
com.mk.www.smsmonitor
├─ common (공통 계층)
│  ├─ api (공통 응답 DTO - ResultDTO, ApiResponse)
│  ├─ config (Security, Web, OpenAPI 설정)
│  └─ util (JwtTokenProvider 등 공통 유틸)
├─ user (사용자 도메인)
│  ├─ api (AuthController, DTO)
│  ├─ application (AuthService, UserDetails)
│  ├─ domain (User 모델)
│  └─ infrastructure (UserEntity, JpaRepository)
├─ transaction (거래/지출 도메인)
│  ├─ api (TransactionController, DTO)
│  ├─ application (TransactionService, SmsService, Port 인터페이스)
│  ├─ domain (Transaction 모델, 분석 전략 인터페이스)
│  └─ infrastructure
│     ├─ parser (카드사별 SMS 파서 - KB, NH)
│     ├─ analysis (멍청비용 분석 전략 구현체)
│     ├─ exporter (구글 시트 연동 및 데이터 내보내기)
│     └─ persistence (TransactionEntity, Mapper)
└─ category (카테고리 도메인)
   ├─ api (SpendingCategoryController, DTO)
   ├─ application (SpendingCategoryService)
   ├─ domain (SpendingCategory 모델)
   └─ infrastructure (Entity, Mapper, Repository 구현체)
```

---
## 주요 API 정의

### 1. 인증 및 회원가입
* `POST /api/auth/register`: 회원가입 요청
* `POST /api/auth/login`: 로그인 및 토큰 발급
* `POST /api/auth/refresh`: 리프레시 토큰을 이용한 액세스 토큰 갱신

### 2. 거래 내역 (Transaction)
* `POST /api/transactions/sms`: SMS 전문을 전송하여 지출 내역 등록
* `GET /api/transactions`: 전체 지출 내역 조회 (페이징 지원, 멍청비용 필터 가능)
* `PUT /api/transactions/{id}/memo`: 특정 지출 내역에 메모 추가

### 3. 카테고리 (Category)
* `GET /api/categories`: 등록된 모든 카테고리 목록 조회
* `POST /api/categories`: 신규 소비 카테고리 등록

---
## 📄 API 문서

애플리케이션 실행 후, 다음 URL로 접속하여 API 문서를 확인하고 직접 API를 테스트해볼 수 있습니다.

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


---
## 핵심 설계 고려 사항
1. **전략 패턴을 이용한 지출 분석**: `StupidCostStrategy` 인터페이스를 통해 새로운 분석 조건(예: 특정 가맹점 차단 등)을 손쉽게 추가할 수 있도록 설계했습니다.
2. **어댑터 패턴을 이용한 SMS 파싱**: 카드사마다 다른 SMS 전문 형식을 처리하기 위해 `SmsParser` 인터페이스를 구현하여 확장성을 확보했습니다.
3. **인프라 계층 분리**: 구글 시트 API 연동(`GoogleSheetExporter`)과 같은 외부 라이브러리 의존성을 `infrastructure` 패키지로 격리하여 비즈니스 로직의 순수성을 유지했습니다.
4. **DDD 레이어드 구조**: 도메인 모델(Entity)과 외부 표현형(DTO)을 분리하고, 각 도메인 모듈이 독립적인 책임을 갖도록 구성했습니다.

---
## 🚀 실행 방법

### 1. 사전 요구사항

- Java 17 (JDK)
- Gradle
- MySQL 데이터베이스

### 2. 프로젝트 클론

```bash
git clone https://github.com/Mkw-k/SmsMonitor.git
cd SmsMonitor
```

### 3. 설정

#### 데이터베이스 설정

`src/main/resources/application.yml` 파일을 열고, 본인의 MySQL 데이터베이스 환경에 맞게 `spring.datasource` 설정을 수정이 필요합니다.
개발시에는 일단은 docker를 사용하여 mysql로 개발하였습니다.
추후에 운영용 profile 추가를 통한 profile 분리예정


```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sms_monitor?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

#### Google Credentials 설정

Google Cloud Platform에서 OAuth 2.0 클라이언트 ID를 생성하고, 인증 정보가 담긴 `json` 파일을 다운로드합니다. 해당 파일의 이름을 `smsreceiver.json`으로 변경한 후, `data/important/` 디렉토리에 위치시킵니다.

또한, `GoogleSheetExporter.java` 파일에 있는 `SPREADSHEET_ID`를 본인의 구글 시트 ID로 변경해야 합니다.

### 4. 애플리케이션 실행

다음 명령어를 사용하여 애플리케이션을 실행합니다.

```bash
./gradlew bootRun
```