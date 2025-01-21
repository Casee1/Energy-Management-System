package com.example.sensorsimulation.service;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class RabbitMqProducer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSensorData(String message) {
        rabbitTemplate.convertAndSend("sensor_data", message);
        logger.info("Sent message: {}", message);
    }
}
