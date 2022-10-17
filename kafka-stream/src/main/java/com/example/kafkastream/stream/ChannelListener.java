package com.example.kafkastream.stream;

import com.example.kafkastream.domain.UserMessageProducer;
import com.example.kafkastream.service.UserMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;

@RequiredArgsConstructor
@Configuration
public class ChannelListener {
    private final UserMessageService userMessageService;
    private final StreamChannel streamChannel;

    @StreamListener(value = "user-input-message")
    public void handleMessage(UserMessageProducer userMessageProducer) {

        if (userMessageProducer.getSentCount() < 3)
            throw new RuntimeException();

        System.out.println("Message Processed Successfully ");
    }

    @StreamListener(value = "user-input-message-dlq")
    public void handleDLQMessage(String message) {

        try {
            UserMessageProducer userMessageProducer = new ObjectMapper().readValue(message, UserMessageProducer.class);

            System.out.println("Catched Failed Message with Sent Count: " + userMessageProducer.getSentCount());
            if (userMessageProducer.getSentCount() < 3) {
                userMessageProducer.setSentCount(userMessageProducer.getSentCount() + 1);
                streamChannel.userOutputMessageChannel().send(
                        MessageBuilder.withPayload(
                                userMessageProducer
                        ).build()
                );
            } else {
                System.out.println("Message sent count exceed: " + message);
                //Store in DB
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Message could not deserialized: " + message);
        }
    }
}
