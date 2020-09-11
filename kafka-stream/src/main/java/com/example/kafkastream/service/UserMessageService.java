package com.example.kafkastream.service;

import com.example.kafkastream.domain.UserMessage;

public interface UserMessageService {
    void sendUserMessage(String message);

    void processReceivedMessage(UserMessage message);
}
