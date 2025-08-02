package com.assignment.userservice.exception;

public class KakaoApiErrorException extends BusinessException {
    public KakaoApiErrorException(String message) {
        super(message);
    }
}
