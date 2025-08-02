package com.assignment.userservice.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoSendApiRequest {
    private int startAge;
    private int endAge;
}
