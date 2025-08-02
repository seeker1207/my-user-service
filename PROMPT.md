## 프롬프트 목록

1. **JPA에서 객체를 생성할 때 어떤 과정을 거치는지?**
   - 엔티티 객체 생성 및 영속성 관리 과정 설명.
   - Builder 패턴 사용, 프록시 객체 생성, 영속성 컨텍스트 등을 포함한 상세 과정.
2. **JPA에서 Builder를 통해 엔티티 객체를 생성하고 save를 호출하면 어떤 과정을 거치는지?**
   - Builder로 객체 생성, `save()` 호출 시 동작, 영속성 컨텍스트 처리 및 트랜잭션 커밋 과정을 설명.
3. **JPA의 CrudRepository가 엔티티의 각 컬럼의 CRUD 메서드를 자동으로 생성해주는지?**
4. **Spring Boot에서 도커이미지를 자동으로 같이 실행하는 방법**?
5. **Java Spring Boot 테스트 코드에서 Docker이미지는 어떻게 이용할수 있는지?**:
   - 예시 코드에서 `@SpringBootTest`와 `Testcontainers`를 활용한 데이터베이스 통합 테스트를 확인했음.
6. **Global Exception 처리 관련 질문**
   - "global Exception 처리를 해야할까? 서비스단의 중복회원 처리 오류 같은 것"

6. **JPA Repository 페이지네이션 관련 질문**
   - "JPA Repository에서 회원 목록을 페이지네이션 기반으로 조회할 수 있으려면?"

7. **Spring 에서 RabbitMQ 설정방법**
   - 큐, DLQ, 라우터 설정방법
8. **RabbitMQ Testcontainers 설정 방법 요청**
   - RabbitMQ를 `Testcontainers`를 사용하여 환경을 구성하는 방법 요청.
   - Docker Compose 기반 설정을 바탕으로 Testcontainers로 변환 요청.
9. **`./gradlew bootRun` 명령 실행 문제 해결 요청**
   - 프로젝트 빌드는 성공하지만, **`./gradlew bootRun`** 실행 후 애플리케이션이 실행되지 않고 종료되는 문제 해결 요청.

10. **대량의 회원데이터를 jpa data 로 부터 Pagenation으로 받아올려면?**
11. **RabbitMq 리스너에서 dlq를 처리하려면?**
12. **카카오메세지 처럼 분당 제한이 걸린 요청을 하려면?**
13. **webclient를 통해 api를 요청하는 예시를 보여줘.**