package com.assignment.userservice.dto;

import com.assignment.userservice.entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetailResponse {
    private final String userId;
    private final String name;
    private final String address;
    private final String phoneNumber;
    private final String citizenNumber;

    public static UserDetailResponse of(Users users) {
        if (users == null) {
            return null;
        }
        // 가장 큰 단위의 행정구역만 제공하도록 주소값 변환
        String[] addressArray = users.getAddress().split(" ");

        return UserDetailResponse.builder()
                .userId(users.getUserId())
                .name(users.getName())
                .address(addressArray[0])
                .phoneNumber(users.getPhoneNumber())
                .citizenNumber(users.getCitizenNumber())
                .build();
    }
}
