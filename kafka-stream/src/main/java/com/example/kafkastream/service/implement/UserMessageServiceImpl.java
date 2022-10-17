package com.example.kafkastream.service.implement;

import com.example.kafkastream.domain.UserMessageConsumer;
import com.example.kafkastream.domain.UserMessageProducer;
import com.example.kafkastream.service.UserMessageService;
import com.example.kafkastream.stream.StreamChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserMessageServiceImpl implements UserMessageService {

    private final StreamChannel streamChannel;

    @Override
    public void sendUserMessage(String message) {
        streamChannel.userOutputMessageChannel().send(
                MessageBuilder.withPayload(
                        UserMessageProducer.builder()
                                .sentCount(1)
                                .message(message)
                                .build()
                ).build()
        );
    }

    @Transactional(isolation = )
    @Override
    public void processReceivedMessage(UserMessageConsumer message) {
        System.out.println("User Received Message: " + message.getAge());
        throw new RuntimeException();
    }
}
