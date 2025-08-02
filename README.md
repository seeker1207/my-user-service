



## 🚀 실행 방법

- ./gradlew build 후 ./gradlew bootRun
- 혹은 IntelliJ에서 실행

- 기본적으로 필요한 도커설정은 compose.yaml에 되어있으며 따로 도커를 실행하지 않아도 spring-boot-docker-compose 패키지를 통해 Spring boot 실행시 자동으로 도커 컨테이너가 실행됩니다.
- 테스트실행 ./gradlew test
- 테스트 또한 TestContainer를 통해 자동으로 도커 컨테이너가 실행됩니다.



## 🛠️ 기술 스택

- **Java 21**
- Spring Boot 3.x
  - Spring MVC
  - Spring AMQP
  - Spring WebFlux(Webclient 사용)
  - Spring Data JPA
  - Spring security
- **RabbitMQ**
- **PostgreSQL**
- **Lombok**



## 📌 Git 전략

- 커밋 메시지는 `feat`, `chore`, `fix`, 등 카테고리별로 분류하여 작성했습니다.



## 🗂 폴더 구조

1. ```
   📦user-service
    ┣ 📂config         # RabbitMQ 및 기타 설정 파일
    ┣ 📂controller     # REST API 엔드포인트
    ┣ 📂dto            # 데이터 송수신 모델
    ┣ 📂entity         # JPA 엔티티
    ┣ 📂repository     # 데이터베이스 접근 기능
    ┣ 📂service        # 비즈니스 로직 및 RabbitMQ 메시지 처리
    ┣ 📂utils          # 유틸리티 클래스
    ┣ 📜application.yml # RabbitMQ 및 데이터베이스 설정
    ┗ 📜README.md      # 프로젝트 설명 파일
   ```



## 📥 Rest API

- public api는 /api/public, 관리자 api 는 /api/admin 이며 basic auth가 필요합니다.

- 회원가입 API 

  - POST /api/public/users

  - body 

  - {

      "userId": "admin",

      "password": "1212",

      "name": "홍길동",

      "citizenNumber": "0002043334567",

      "phoneNumber": "01012344678",

      "address": "서울특별시 강남구 테헤란로 123 푸른빌딩 4층"

    }

- 로그인한 회원 상세 조회 

  - GET /api/public/users/me

- 회원 페이지네이션 조회 API 

  - GET /api/admin/users?page=0&size=3

- 회원 수정

  - PUT /api/admin/users?userId=testuser1&newPassword=1234&newAddress=서울시관악구

- 회원 삭제

  - DELETE /api/admin/users?userId=testuser1

- 로그인API 

  - POST /api/public/users/login

  - body {

    ​    "userId": "admin",

    ​    "password": "1212" 

    }

- 연령별 회원 카카오 메세지 전송 API (POST /api/admin/send/kakao)

  - POST /api/admin/send/kakao

  - body {

    ​    "startAge": 20,

    ​    "endAge": 29

    }



## ⚙️ 주요 기술 구현설명

## Data 계층

기본적으로 Spring Data JPA를 사용했으며 Users Entity를 중심으로 Query 메서드로 DB와 연결합니다.

## Spring Security

basic auth와 로그인 처리 등 인증 및 인가 처리는 기본적으로 Spring Security에서 제공하는 기능을 이용하였습니다.

## RabbitMQ을 통한 카카오메세지 비동기처리

pagination을 통해 연령별 전체 회원을 전부 db에서 받아오지 않고 나누어서 받아오도록 처리하였습니다. 

chunk 처리된 메세지 전송은 기본적으로 RabbitMQ를 통해 비동기처리하였으며 처음 카카오 발송에 실패한경우 Dead Letter Queue(DLQ)를 이용하여 실패 메시지를 sms 메세지로 전송 처리하였습니다.

1분당의 100회, 500회의 제한사항을 위해 RateLimeter를 이용하였습니다.

## 테스트 

Service 단과 Repository의 통합 테스트를 구축하였습니다.

기본적으로 TestContainer를 통해 도커이미지와 함께 독립된 테스트 환경을 구축하였습니다.





