package com.example.kafkaprotobuf;

import com.example.kafkaprotobuf.proto.MessageProto;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.PipedInputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MessagePublisherController {
    @Autowired
    private Producer<String, byte[]> producer;

    @PostMapping
    public void handler(@RequestBody MessageBodyRequest messageBodyRequest) {
        MessageProto.Message message = MessageProto.Message.newBuilder().setId(
                        messageBodyRequest.id
                ).setMessage(messageBodyRequest.message)
                .setSubject(messageBodyRequest.subject).build();

        producer.send(
                new ProducerRecord<>("test", messageBodyRequest.id + "", message.toByteArray())
        );
    }

    @KafkaListener(
            topics = {"test"},
            groupId = "kafka-protobuf",
            containerFactory = "protoMessageListener"
    )
    public void messageListener(byte[] binaryMessage) throws InvalidProtocolBufferException {
        MessageProto.Message message = MessageProto.Message.parseFrom(binaryMessage);
        System.out.println("Message: " + message.getMessage());
    }

    public static class MessageBodyRequest {
        private int id;
        private String message;
        private String subject;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }
}
