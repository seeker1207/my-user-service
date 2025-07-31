package com.assignment.userservice.exception;

import com.assignment.userservice.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // 모든 REST 컨트롤러에 대해 전역 예외 처리를 적용
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직에서 발생한 사용자 정의 예외를 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        log.error("BusinessException occurred: {}", ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.of(
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                "Business Exception"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 서버 오류 등 예상치 못한 일반 예외를 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.of(
                "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.",
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}