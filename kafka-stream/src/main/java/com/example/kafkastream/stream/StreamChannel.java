package com.example.kafkastream.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface StreamChannel {
    @Output(value = "user-output-message")
    MessageChannel userOutputMessageChannel();

    @Input(value = "user-input-message")
    MessageChannel userInputMessageChannel();

    @Input(value = "user-input-message-dlq")
    MessageChannel userInputMessageDLQChannel();
}
