package com.assignment.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient kakaoWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://localhost:8081/kakaotalk-messages")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuth("autoever", "1234"))
                .build();
    }

    @Bean
    public WebClient smsWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://localhost:8082/sms?phone=-messages")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuth("autoever", "5678"))
                .build();
    }

    private static String basicAuth(String username, String password) {
        String credentials = username + ":" + password;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encoded;
    }
}
