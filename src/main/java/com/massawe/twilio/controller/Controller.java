package com.massawe.twilio.controller;

import com.massawe.twilio.services.Service;
import com.massawe.twilio.services.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("send/sms")
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {
    private final Service service;

    @Autowired
    public Controller(Service service) {
        this.service = service;
    }

    @PostMapping
    public void sendSms(@Validated @RequestBody SmsRequest smsRequest) {
        service.sendSms(smsRequest);
    }
}
