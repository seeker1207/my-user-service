package com.assignment.userservice.controller;

import com.assignment.userservice.dto.UserDetailResponse;
import com.assignment.userservice.dto.UserLoginRequest;
import com.assignment.userservice.dto.UserResponse;
import com.assignment.userservice.dto.UserSignupRequest;
import com.assignment.userservice.entity.Users;
import com.assignment.userservice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // 회원가입
    @PostMapping("/api/public/users")
    public UserResponse register(@RequestBody UserSignupRequest userSignupRequest ) {

        return userService.register(userSignupRequest);
    }

    //로그인
    @PostMapping("/api/public/users/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest, HttpSession session) {
        try {
            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getUserId(), userLoginRequest.getPassword());

            // 인증 수행
            Authentication authentication = authenticationManager.authenticate(authRequest);

            // SecurityContext에 인증 정보 저장
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            // 세션에 SecurityContext 저장
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            Users users = (Users) authentication.getPrincipal();

            return ResponseEntity.ok(UserResponse.of(users)) ;
        } catch (AuthenticationException e) {
            log.error("로그인 실패: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인 실패: " + e.getMessage()));

        }
    }
    // 로그인한 회원 상세정보 조회
    @GetMapping("/api/public/users/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Users users = (Users) authentication.getPrincipal();
        return ResponseEntity.ok(UserDetailResponse.of(users));
    }

    @GetMapping("/api/admin/users")
    public Page<UserResponse> searchUsers(@RequestParam int page, @RequestParam int size) {

        return userService.searchUsers(page, size);
    }

    @PutMapping("/api/admin/users")
    public UserResponse modifyUsersPasswordAndAddress(@RequestParam String userId,
                                                      @RequestParam(required = false) String newPassword,
                                                      @RequestParam(required = false) String newAddress) {
        if (newPassword == null && newAddress == null) {
            throw new IllegalArgumentException("패스워드나 주소값 중 하나는 필수 입니다.");
        }
        return userService.modifyUsersPasswordAndAddress(userId, newPassword, newAddress);
    }

    @DeleteMapping("/api/admin/users")
    public void deleteUsers(@RequestParam String userId, HttpServletResponse response) {
        userService.deleteUsers(userId);
        response.setStatus(204);
    }



    @PostMapping("/api/public/users/test")
    public String testRegister() {
        for (int i = 0; i < 15; i++) {
            UserSignupRequest request = UserSignupRequest.builder()
                    .userId("testuser" + i)
                    .password("password123")
                    .name("User " + i)
                    .citizenNumber("99010112345" + String.format("%02d", i))
                    .phoneNumber("010123456" + String.format("%02d", i))
                    .address("서울시 강남구")
                    .build();
            userService.register(request);
        }
        return "OK";
    }
}
