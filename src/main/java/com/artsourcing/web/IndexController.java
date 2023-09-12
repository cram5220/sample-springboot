package com.artsourcing.web;

import com.artsourcing.service.aligo.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@RequiredArgsConstructor
@RestController
public class IndexController {

    private final SmsService smsService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/sms/send")
    public String send(){
        try {
            smsService.sendSms();
            smsService.sendMms();
            return "success";
        } catch (Exception e) {
            return "failure";
        }
    }

}

