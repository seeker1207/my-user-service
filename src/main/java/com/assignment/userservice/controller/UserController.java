package com.assignment.userservice.controller;

import com.assignment.userservice.dto.UserResponse;
import com.assignment.userservice.dto.UserSignupRequest;
import com.assignment.userservice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;


    @PostMapping("/api/public/users")
    public UserResponse register(@RequestBody UserSignupRequest userSignupRequest ) {

        return userService.register(userSignupRequest);
    }

    @GetMapping("/api/admin/users")
    public Page<UserResponse> searchUsers(@RequestParam int page, @RequestParam int size) {

        return userService.searchUsers(page, size);
    }

    @PutMapping("/api/admin/users")
    public UserResponse modifyUsersPasswordAndAddress(@RequestParam String userId, @RequestParam(required = false) String newPassword, @RequestParam(required = false) String newAddress) {
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
