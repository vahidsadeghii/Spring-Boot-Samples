package com.example.outboxsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OutboxsampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(OutboxsampleApplication.class, args);
	}	
}
