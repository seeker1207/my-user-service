package com.assignment.userservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public static ErrorResponse of(String message, String path, int status, String error) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .status(status)
                .error(error)
                .path(path)
                .build();
    }
}