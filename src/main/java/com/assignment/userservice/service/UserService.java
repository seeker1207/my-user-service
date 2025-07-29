package com.assignment.userservice.service;

import com.assignment.userservice.dto.UserResponse;
import com.assignment.userservice.dto.UserSignupRequest;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponse register(UserSignupRequest request);

    Page<UserResponse> searchUsers(int page, int size);

    UserResponse modifyUsersPasswordAndAddress(String userId, String newPassword, String newAddress);

    void deleteUsers(String userId);
}