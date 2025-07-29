package com.assignment.userservice.service;

import com.assignment.userservice.dto.UserResponse;
import com.assignment.userservice.dto.UserSignupRequest;
import com.assignment.userservice.entity.Users;
import com.assignment.userservice.enums.UserRole;
import com.assignment.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public UserResponse register(UserSignupRequest request) {
        // 중복 검증
//        validateDuplicateUser(request.getUserId());
        
        // 비즈니스 로직 처리
        Users user = createUsers(request);
        Users savedUsers = userRepository.save(user);
        
        // 응답 생성
        return UserResponse.of(savedUsers);
    }
    
//    private void validateDuplicateUsers(String userId) {
//        if (userRepository.existsByUsersId(userId)) {
//            throw new Users()Exception("이미 존재하는 사용자 ID입니다.");
//        }
//    }
    
    private Users createUsers(UserSignupRequest request) {
        return Users.builder()
                .userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .citizenNumber(request.getCitizenNumber())
                .role(UserRole.USER)  // 기본 역할 설정
                .build();
    }
}
