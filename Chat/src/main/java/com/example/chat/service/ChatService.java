package com.example.chat.service;

import com.example.chat.entity.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public void sendMessage(Chat chat) {
        messagingTemplate.convertAndSendToUser(chat.getRecipient(), "/queue/private", chat);
        System.out.println("Message sent to /user/" + chat.getRecipient() + "/queue/private" + chat.getRecipient());
    }


    public Chat broadcastMessage(Chat chat) {
        messagingTemplate.convertAndSend("/topic/public", chat);
        return  chat;
    }
}
