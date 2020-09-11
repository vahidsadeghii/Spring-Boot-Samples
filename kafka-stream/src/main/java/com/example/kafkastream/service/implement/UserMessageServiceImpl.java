package com.example.kafkastream.service.implement;

import com.example.kafkastream.domain.UserMessage;
import com.example.kafkastream.service.UserMessageService;
import com.example.kafkastream.stream.StreamChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserMessageServiceImpl implements UserMessageService {

    private final StreamChannel streamChannel;

    @Override
    public void sendUserMessage(String message) {
        streamChannel.userOutputMessageChannel().send(
                MessageBuilder.withPayload(
                        UserMessage.builder()
                                .message(message)
                                .build()
                ).build()
        );
    }

    @Override
    public void processReceivedMessage(UserMessage message) {
        System.out.println("User Received Message" + message.getMessage());
    }
}
