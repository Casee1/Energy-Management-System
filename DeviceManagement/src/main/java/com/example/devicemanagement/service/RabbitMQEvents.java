package com.example.devicemanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Component
public class RabbitMQEvents {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQEvents.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEvents(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    private DeviceUserService deviceUserService;

    @RabbitListener(queues = "deleted_user")
    public void deleteUser(String idUserString) {
        UUID idUser = UUID.fromString(idUserString);
        logger.info("Received delete message for user ID: {}", idUser);
        deviceUserService.deleteDeviceUserByUserId(idUser);
        logger.info("Processed delete for DeviceUser with user ID: {}", idUser);
    }


}
