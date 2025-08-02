package com.assignment.userservice.dto;

import com.assignment.userservice.entity.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoSendRequest {
    private String phone;
    private String message;

    public static KakaoSendRequest of(Users users, String message) {
        String userPhoneNumber = users.getPhoneNumber();
        String sendPhoneNumber = userPhoneNumber.substring(0, 3) + "-" +
                userPhoneNumber.substring(3, 7) + "-" +
                userPhoneNumber.substring(7, 11);
        return KakaoSendRequest.builder()
                .phone(sendPhoneNumber)
                .message(message)
                .build();
    }
}
