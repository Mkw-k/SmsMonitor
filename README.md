# SmsMonitor

## 📖 프로젝트 소개

**SmsMonitor**는 신용카드 및 체크카드 사용 시 수신되는 SMS 문자 메시지를 분석하여, 소비 내역을 기록하고 소비패턴을 관리하는 REST API 프로젝트입니다. 이 프로젝트를 통해 사용자는 자신의 소비 패턴을 파악하고, 특히 "멍청비용"(과소비, 불필요한 지출 등)을 식별하여 더 나은 소비 습관을 기를 수 있습니다.

## ✨ 주요 기능

- **SMS 자동 파싱**: 여러 카드사(현재 KB국민카드, NH농협카드 지원)의 다양한 SMS 양식을 파싱하여 거래 내역을 추출합니다.
- **거래 내역 관리**: 생성된 거래 내역을 데이터베이스에 저장하고, CRUD 기능을 제공합니다.
- **소비 카테고리 관리**: 사용자가 직접 소비 카테고리(예: 식비, 교통비)를 생성하고 관리할 수 있습니다.
- **멍청비용 분석**: 사용자가 정의한 전략에 따라 특정 거래를 "멍청비용"으로 자동 분류합니다.
  - 예: 늦은 밤(22시 이후)에 특정 상점에서 결제, 식비 카테고리에서 5만원 이상 결제 등
- **메모 기능**: 각 거래 내역에 대한 메모를 추가하거나 수정할 수 있습니다.
- **Google Sheets 연동**: 분석된 거래 내역을 Google Sheets로 자동 내보내기(export)합니다.

## 🛠️ 기술 스택

- **언어**: Java 17
- **프레임워크**: Spring Boot 3
- **데이터베이스**:
  - Spring Data JPA
  - MySQL (운영)
  - H2 (테스트)
- **빌드 도구**: Gradle
- **라이브러리**:
  - Lombok
  - SpringDoc OpenAPI (Swagger UI)
  - Google API Client for Java (Google Sheets 연동)
- **아키텍처**:
  - 계층형 아키텍처 (Presentation, Application, Domain, Infrastructure)
  - 객체지향 설계 원칙 (SRP, CQRS 등) 적용

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

Google Cloud Platform에서 OAuth 2.0 클라이언트 ID를 생성하고, 인증 정보가 담긴 `json` 파일을 다운로드합니다. 해당 파일의 이름을 `smsreceiver.json`으로 변경한 후, `src/main/resources/credentials/` 디렉토리에 위치시킵니다.

또한, `GoogleSheetExporter.java` 파일에 있는 `SPREADSHEET_ID`를 본인의 구글 시트 ID로 변경해야 합니다.

### 4. 애플리케이션 실행

다음 명령어를 사용하여 애플리케이션을 실행합니다.

```bash
./gradlew bootRun
```

## 📄 API 문서

애플리케이션 실행 후, 다음 URL로 접속하여 API 문서를 확인하고 직접 API를 테스트해볼 수 있습니다.

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

##  주요 API 엔드포인트

- `POST /api/transactions/sms`: SMS 메시지를 수신하여 거래내역으로 저장
- `GET /api/transactions`: 모든 거래내역 페이징 조회
- `GET /api/transactions?isStupid=true`: "멍청비용"으로 분류된 내역만 조회
- `PUT /api/transactions/{id}/memo`: 특정 거래내역에 메모 수정/추가
- `POST /api/spending-categories`: 새로운 소비 카테고리 생성
- `GET /api/spending-categories`: 모든 소비 카테고리 조회
- `PUT /api/spending-categories/{id}`: 특정 소비 카테고리 정보 수정
- `DELETE /api/spending-categories/{id}`: 특정 소비 카테고리 삭제
