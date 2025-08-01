package com.assignment.userservice;

import com.assignment.userservice.entity.Users;
import com.assignment.userservice.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest  // JPA 컴포넌트만 테스트하기 위한 어노테이션
@Testcontainers
@Transactional
public class UsersRepositoryTests {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("mydb")
            .withUsername("user")
            .withPassword("pass1234");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    @Test
    @DisplayName("사용자 생성 테스트")
    void createUserTest() {
        // given
        Users users = Users.builder()
                .userId("testUser")
                .password("password123")
                .name("Test Name")
                .citizenNumber("9804203210124")
                .phoneNumber("01012345678")
                .address("Test Address")
                .build();

        // when
        Users savedUsers = userRepository.save(users);
        entityManager.flush();

        // then
        assertNotNull(savedUsers.getId());
        assertEquals(users.getUserId(), savedUsers.getUserId());
        assertEquals(users.getName(), savedUsers.getName());
    }

    @Test
    @DisplayName("사용자 조회 테스트")
    void findUserTest() {
        // given
        Users users = Users.builder()
                .userId("findTestUser")
                .password("password123")
                .name("Find Test")
                .citizenNumber("9804203210123")
                .phoneNumber("01087654321")
                .address("Find Test Address")
                .build();
        Users savedUsers = userRepository.save(users);

        // when
        Optional<Users> foundUser = userRepository.findByUserId(savedUsers.getUserId());

        // then
        assertTrue(foundUser.isPresent());
        assertEquals(users.getUserId(), foundUser.get().getUserId());
    }

    @Test
    @DisplayName("사용자 전체 조회 테스트")
    void findAllUsersTest() {
        // given
        Users users1 = Users.builder()
                .userId("user1")
                .password("pass1")
                .name("User One")
                .citizenNumber("9804203210123")
                .phoneNumber("01011111111")
                .address("Address 1")
                .build();

        Users users2 = Users.builder()
                .userId("user2")
                .password("pass2")
                .name("User Two")
                .citizenNumber("9404203210123")
                .phoneNumber("01022222222")
                .address("Address 2")
                .build();

        userRepository.saveAll(Arrays.asList(users1, users2));

        // when
        Iterable<Users> users = userRepository.findAll();

        // then
        assertNotNull(users);
        assertTrue(StreamSupport.stream(users.spliterator(), false)
                .count() >= 2);
    }

    @Test
    @DisplayName("사용자 업데이트 테스트")
    void updateUserTest() {
        // given
        Users users = Users.builder()
                .userId("updateUser")
                .password("oldPassword")
                .name("Old Name")
                .citizenNumber("9804203210123")
                .phoneNumber("01033333333")
                .address("Old Address")
                .build();
        Users savedUsers = userRepository.save(users);

        // when
        savedUsers.updatePassword("newPassword");
        savedUsers.updateAddress("New Address");
        Users updatedUsers = userRepository.save(savedUsers);

        // then
        assertEquals("newPassword", updatedUsers.getPassword());
        assertEquals("New Address", updatedUsers.getAddress());
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void deleteUserTest() {
        // given
        Users users = Users.builder()
                .userId("deleteUser")
                .password("password123")
                .name("Delete Test")
                .citizenNumber("9804203210123")
                .phoneNumber("01044444444")
                .address("Delete Address")
                .build();
        Users savedUsers = userRepository.save(users);

        // when
        userRepository.delete(savedUsers);

        // then
        Optional<Users> deletedUser = userRepository.findByUserId(savedUsers.getUserId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    @DisplayName("중복된 userId 테스트")
    void duplicateUserIdTest() {
        // given
        Users users1 = Users.builder()
                .userId("sameUserId")
                .password("password1")
                .name("User One")
                .citizenNumber("9804203210123")
                .phoneNumber("01055555555")
                .address("Address 1")
                .build();
        userRepository.save(users1);

        // when & then
        Users users2 = Users.builder()
                .userId("sameUserId")
                .password("password2")
                .name("User Two")
                .citizenNumber("9804203210123")
                .phoneNumber("01066666666")
                .address("Address 2")
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(users2);
            entityManager.flush();
        });
    }



}
