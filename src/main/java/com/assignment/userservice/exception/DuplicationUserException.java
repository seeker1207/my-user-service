package com.assignment.userservice.exception;

public class DuplicationUserException extends RuntimeException {
    public DuplicationUserException(String message) {
        super(message);
    }
}
