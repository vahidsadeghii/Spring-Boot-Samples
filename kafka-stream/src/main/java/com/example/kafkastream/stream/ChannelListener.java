package com.example.kafkastream.stream;

import com.example.kafkastream.domain.UserMessage;
import com.example.kafkastream.service.UserMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ChannelListener {
    private final UserMessageService userMessageService;

    @StreamListener(value = "user-input-message")
    public void handleMessage(UserMessage message) {
        userMessageService.processReceivedMessage(message);
    }
}
