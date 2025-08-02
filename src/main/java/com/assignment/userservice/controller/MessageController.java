package com.assignment.userservice.controller;

import com.assignment.userservice.dto.KakaoSendApiRequest;
import com.assignment.userservice.exception.BusinessException;
import com.assignment.userservice.service.KakaoMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final KakaoMessageService kakaoMessageService;

    @PostMapping("/api/admin/send/kakao")
    public ResponseEntity<?> sendKakaoMessage(@RequestBody KakaoSendApiRequest kakaoSendApiRequest) {
        if (kakaoSendApiRequest.getStartAge() > kakaoSendApiRequest.getEndAge()) {
            throw new BusinessException("시작나이가 끝나이보다 작아야 합니다.");
        }

        kakaoMessageService.sendKakaoMessageByAgeGroup(
                kakaoSendApiRequest.getStartAge(), kakaoSendApiRequest.getEndAge());

        return ResponseEntity.ok().build();
    }
}
