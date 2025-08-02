package com.assignment.userservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.kakao}")
    private String queueName;

    @Value("${rabbitmq.exchange.kakao}")
    private String exchangeName;

    @Value("${rabbitmq.routing.kakao}")
    private String routingKey;

    @Bean
    public Queue messageQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", exchangeName + ".dlq")
                .withArgument("x-dead-letter-routing-key", routingKey + ".dlq")
                .build();
    }

    @Bean
    public DirectExchange messageExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue messageQueue, DirectExchange messageExchange) {
        return BindingBuilder
                .bind(messageQueue)
                .to(messageExchange)
                .with(routingKey);
    }

    // Dead Letter Queue 설정
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(queueName + ".dlq");
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(exchangeName + ".dlq");
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(routingKey);
    }
}

