package com.assignment.userservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsSendRequest {
    private String message;

    public static SmsSendRequest of(KakaoSendRequest kakaoSendRequest) {
        return SmsSendRequest.builder()
                .message(kakaoSendRequest.getMessage())
                .build();
    }
}
