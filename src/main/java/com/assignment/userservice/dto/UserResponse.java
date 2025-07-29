package com.assignment.userservice.dto;

import com.assignment.userservice.entity.Users;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {

    private final Long id;
    private final String userId;
    private final String name;

    public static UserResponse of(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .name(user.getName())
                .build();
    }

}
