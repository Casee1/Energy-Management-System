package com.example.chat.controller;

import com.example.chat.entity.Chat;
import com.example.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
//@CrossOrigin(origins = "http://react.localhost")
//@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/sendMessage")
    public void sendMessage(Chat chat) {
        System.out.println("Sending message to:" + chat.getRecipient());
        chatService.sendMessage(chat);
    }

    @MessageMapping("/broadcast")
    public Chat broadcastMessage(Chat chat) {
        return chatService.broadcastMessage(chat);
    }
}
