package com.assignment.userservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserSignupRequest {

    private String userId;
    private String password;
    private String name;
    private String citizenNumber;
    private String phoneNumber;
    private String address;

}
