package com.assignment.userservice.service;

import com.assignment.userservice.dto.UserResponse;
import com.assignment.userservice.dto.UserSignupRequest;

public interface UserService {

    UserResponse register(UserSignupRequest request);

}
