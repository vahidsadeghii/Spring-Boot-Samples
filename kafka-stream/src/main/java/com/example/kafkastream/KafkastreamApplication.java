package com.example.kafkastream;

import com.example.kafkastream.stream.StreamChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(StreamChannel.class)
public class KafkastreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkastreamApplication.class, args);
    }


}
