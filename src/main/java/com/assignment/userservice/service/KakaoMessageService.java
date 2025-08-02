package com.assignment.userservice.service;

import com.assignment.userservice.dto.KakaoSendRequest;
import com.assignment.userservice.dto.SmsSendRequest;
import com.assignment.userservice.entity.Users;
import com.assignment.userservice.exception.KakaoApiErrorException;
import com.assignment.userservice.repository.UserRepository;
import com.assignment.userservice.utils.DateRange;
import com.assignment.userservice.utils.Utils;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoMessageService {
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    private final RateLimiter rate100PerOneMinuteLimiter;
    private final RateLimiter rate500PerOneMinuteLimiter;
    private final WebClient kakaoWebClient;
    private final WebClient smsWebClient;


    @Value("${rabbitmq.exchange.kakao}")
    private String kakaoExchange;

    @Value("${rabbitmq.routing.kakao}")
    private String kakaoRoutingKey;


    @Transactional(readOnly = true)
    public void sendKakaoMessageByAgeGroup(int startAge, int endAge) {
        DateRange dateRange = Utils.getBirthDateRangeForAgeGroup(startAge, endAge, LocalDate.now());

        int pageSize = 1;
        int pageNumber = 0;
        Page<Users> page;
        do {
            page = userRepository.findByBirthDateBetween(
                    dateRange.startDate(),
                    dateRange.endDate(),
                    PageRequest.of(pageNumber, pageSize)
            );

            List<Users> userInfoList = page.getContent();
            userInfoList.forEach(user -> {
                KakaoSendRequest kakaoSendRequest =
                        KakaoSendRequest.of(user, makeMessage(user.getName()));
                rabbitTemplate.convertAndSend(kakaoExchange, kakaoRoutingKey, kakaoSendRequest);
                log.info("메시지 발행: {}", kakaoSendRequest.toString());

            });

            pageNumber++;

        } while (page.hasNext());

    }

    @RabbitListener(queues = "${rabbitmq.queue.kakao}")
    public void processMessage(KakaoSendRequest kakaoSendRequest) {
        try {
            // 레이트 리미터: 초당 1.67개 호출 허용 (1분에 100회)
            rate100PerOneMinuteLimiter.tryAcquire();

            // 메시지 발송
            sendKakaoMessage(kakaoSendRequest);
            log.info("메시지 발송 요청 성공: {}", kakaoSendRequest);

        } catch (Exception e) {
            log.error("메시지 발송 실패: {}", kakaoSendRequest, e);
            throw new AmqpRejectAndDontRequeueException("DLQ로 메시지 이동", e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.kakao}.dlq")
    public void processDeadLetterMessage(KakaoSendRequest kakaoSendRequest) {
        try {
            log.warn("DLQ에서 메시지 처리 시작: {}", kakaoSendRequest);

            sendSmsMessage(SmsSendRequest.of(kakaoSendRequest));

        } catch (Exception e) {
            log.error("DLQ 메시지 처리 실패: {}", kakaoSendRequest, e);
        }
    }


    public void sendKakaoMessage(KakaoSendRequest kakaoSendRequest) {
        kakaoWebClient.post()
                .bodyValue(kakaoSendRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .map(KakaoApiErrorException::new);
                })
                .bodyToMono(String.class)
                .block();

    }

    private String makeMessage(String userName) {
        return String.format("%s님, 안녕하세요. 현대 오토에버입니다.", userName);
    }


    public void sendSmsMessage(SmsSendRequest smsSendRequest) {
        rate500PerOneMinuteLimiter.tryAcquire();
        smsWebClient.post()
                .bodyValue(smsSendRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .map(KakaoApiErrorException::new);
                })
                .bodyToMono(String.class)
                .subscribe(
                        response -> {
                            log.info("sms 메세지 발송 성공 api 응답: {}", response);
                        }, error -> {
                            log.error("sms 메세지 발송 실패: ", error);
                        }
                );
    }
}
