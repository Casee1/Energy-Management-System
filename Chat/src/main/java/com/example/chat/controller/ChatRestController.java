package com.example.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatRestController {

    @GetMapping("/test")
    public String testEndpoint() {
        return "Chat microservice is running";
    }
}
