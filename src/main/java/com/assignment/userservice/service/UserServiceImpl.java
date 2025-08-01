package com.assignment.userservice.service;

import com.assignment.userservice.dto.UserResponse;
import com.assignment.userservice.dto.UserSignupRequest;
import com.assignment.userservice.entity.Users;
import com.assignment.userservice.enums.UserRole;
import com.assignment.userservice.exception.DuplicationUserException;
import com.assignment.userservice.exception.UserNotFoundException;
import com.assignment.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public UserResponse register(UserSignupRequest request) {
        // 중복 검증
        validateDuplicateUsers(request.getUserId(), request.getCitizenNumber());
        
        // 비즈니스 로직 처리
        Users user = createUsers(request);
        Users savedUsers = userRepository.save(user);
        
        // 응답 생성
        return UserResponse.of(savedUsers);
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Users> usersPage = userRepository.findAll(pageable);
        return usersPage.map(UserResponse::of);
    }

    @Transactional
    public UserResponse modifyUsersPasswordAndAddress(String userId, String newPassword, String newAddress) {
        Users user = userRepository.findByUserId(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("사용자 ID '%s'를 찾을 수 없습니다.", userId)
        ));

        if (newPassword != null) {
            user.updatePassword(passwordEncoder.encode(newPassword));
        }
        if (newAddress != null) {
            user.updateAddress(newAddress);
        }


        return UserResponse.of(user);
    }

    @Transactional
    public void deleteUsers(String userId) {
        Users user = userRepository.findByUserId(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("사용자 ID '%s'를 찾을 수 없습니다.", userId)
        ));

        user.delete();
    }

    private void validateDuplicateUsers(String userId, String citizenNumber) {
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new DuplicationUserException("이미 존재하는 사용자 ID입니다.");
        }
        if (userRepository.findByCitizenNumber(citizenNumber).isPresent()) {
            throw new DuplicationUserException("이미 존재하는 주민등록번호입니다.");
        }
    }

    private Users createUsers(UserSignupRequest request) {
        return Users.builder()
                .userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .citizenNumber(request.getCitizenNumber())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();
    }
}
