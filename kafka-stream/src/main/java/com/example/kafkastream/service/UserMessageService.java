package com.example.kafkastream.service;

import com.example.kafkastream.domain.UserMessageConsumer;

public interface UserMessageService {
    void sendUserMessage(String message);

    void processReceivedMessage(UserMessageConsumer message);
}
