package com.springbootsample.multipartrequest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SMSService {
    @Async
    public void sendSMS(String message) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("SMS Sent");
    }
}
