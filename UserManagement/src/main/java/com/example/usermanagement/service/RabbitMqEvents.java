package com.example.usermanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Component
public class RabbitMqEvents {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqEvents.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqEvents(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendUserDeletedEvent(UUID id) {
        logger.info("Sending delete event for user ID: {}", id);
        rabbitTemplate.convertAndSend("deleted_user", id.toString());
    }
}
