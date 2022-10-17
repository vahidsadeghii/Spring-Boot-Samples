package com.springbootsample.multipartrequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    SMSService smsService;

    public String payment() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        smsService.sendSMS("Message");

        return "Done";
    }
}
