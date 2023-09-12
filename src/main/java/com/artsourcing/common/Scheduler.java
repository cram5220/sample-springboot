package com.artsourcing.common;

import com.artsourcing.domain.MmsSendRepository;
import com.artsourcing.domain.SmsSendRepository;
import com.artsourcing.service.aligo.AligoService;
import com.artsourcing.service.aligo.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Scheduler {

    private final SmsService smsService;

    @Scheduled(cron = "0/2 * * * * *")
    public void sendAutoSms(){
        smsService.sendSms();
        smsService.sendMms();
    }

}
