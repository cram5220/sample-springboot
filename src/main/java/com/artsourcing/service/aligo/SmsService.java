package com.artsourcing.service.aligo;

import com.artsourcing.domain.MmsSend;
import com.artsourcing.domain.MmsSendRepository;
import com.artsourcing.domain.SmsSend;
import com.artsourcing.domain.SmsSendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SmsService {


    private final SmsSendRepository smsRepository;
    private final MmsSendRepository mmsRepository;

    private final AligoService aligoService;

    public void sendSms(){
        List<SmsSend> list = smsRepository.findAll();

        for(SmsSend sms : list){
            sendSmsAndDelete(sms);
        }

    }

    @Transactional
    public void sendSmsAndDelete(SmsSend sms){
        try {
            String receiver = sms.getDestInfo().split("\\^")[1];
            aligoService.sendAligoSms(receiver,sms.getSmsMsg());
            smsRepository.delete(sms);
        }catch (Exception e) {
            System.out.println("문자 전송 실패----");
            System.out.println(sms.getDestInfo());
            System.out.println(sms.getSmsMsg());
            smsRepository.delete(sms);
        }
    }

    public void sendMms(){
        List<MmsSend> list = mmsRepository.findAll();

        for(MmsSend mms : list){
            sendMmsAndDelete(mms);
        }
    }

    @Transactional
    public void sendMmsAndDelete(MmsSend mms){
        try {
            String receiver = mms.getDestInfo().split("\\^")[1];
            aligoService.sendAligoSms(receiver,mms.getMmsMsg());
            mmsRepository.delete(mms);
        }catch (Exception e) {
            System.out.println("문자 전송 실패----");
            System.out.println(mms.getDestInfo());
            System.out.println(mms.getMmsMsg());
            mmsRepository.delete(mms);
        }
    }

}
