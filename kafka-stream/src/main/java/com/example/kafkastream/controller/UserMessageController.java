package com.example.kafkastream.controller;

import com.example.kafkastream.service.UserMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserMessageController {
    private final UserMessageService userMessageService;

    @PostMapping("/user-messages")
    public void handle(@RequestBody UserMessageRequest request) {
        userMessageService.sendUserMessage(request.getMessage());
    }
}
