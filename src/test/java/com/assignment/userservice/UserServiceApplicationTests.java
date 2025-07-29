package com.assignment.userservice;

import com.assignment.userservice.dto.UserSignupRequest;
import com.assignment.userservice.dto.UserResponse;
import com.assignment.userservice.entity.Users;
import com.assignment.userservice.exception.DuplicationUserException;
import com.assignment.userservice.repository.UserRepository;
import com.assignment.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class UserServiceApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("mydb")
            .withUsername("user")
            .withPassword("pass1234");

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 통합 테스트")
    void registerIntegrationTest() {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .userId("testuser")
                .password("password123")
                .name("홍길동")
                .citizenNumber("9901011234567")
                .phoneNumber("01012345678")
                .address("서울시 강남구")
                .build();

        // when
        UserResponse response = userService.register(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(request.getUserId());
        assertThat(response.getName()).isEqualTo(request.getName());

        // DB 확인
        Users savedUser = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        assertThat(savedUser.getUserId()).isEqualTo(request.getUserId());
        assertThat(savedUser.getName()).isEqualTo(request.getName());
        assertThat(savedUser.getCitizenNumber()).isEqualTo(request.getCitizenNumber());
    }

    @Test
    @DisplayName("중복 회원 가입 통합 테스트")
    void registerDuplicateUserIntegrationTest() {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .userId("testuser")
                .password("password123")
                .name("홍길동")
                .citizenNumber("9901011234567")
                .phoneNumber("01012345678")
                .address("서울시 강남구")
                .build();

        // 첫 번째 회원가입
        userService.register(request);

        // when & then
        assertThrows(DuplicationUserException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    @DisplayName("중복 주민등록번호 회원 가입 통합 테스트")
    void registerDuplicateCitizenNumberIntegrationTest() {
        // given
        UserSignupRequest request = UserSignupRequest.builder()
                .userId("testuser")
                .password("password123")
                .name("홍길동")
                .citizenNumber("9901011234567")
                .phoneNumber("01012345678")
                .address("서울시 강남구")
                .build();

        // 첫 번째 회원가입
        userService.register(request);

        // 중복 id 우회
        request.setUserId("testuser2");

        // when & then
        assertThrows(DuplicationUserException.class, () -> {
            userService.register(request);
        });
    }
}