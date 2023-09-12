package com.artsourcing.service.aligo;

import com.artsourcing.common.ApiRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class AligoService {
    @Value("${aligo-key}")
    private String aligoKey;

    @Value("${aligo-identifier}")
    private String aligoIdentifier;

    @Value("${aligo-sender}")
    private String aligoSender;

    @Value("${aligo-url}")
    private String aligoUrl;

    // 문자 전송
    public Boolean sendAligoSms(String receiverNumber, String msg){
        ApiRequest aligoApi = setAligoDefaultRequest();
        aligoApi.putRequestBody(new BasicNameValuePair("receiver",receiverNumber));
        aligoApi.putRequestBody(new BasicNameValuePair("msg",msg));
        return alertIfFailure(aligoApi.getResponseJson());
    }

    private ApiRequest setAligoDefaultRequest(){
        ApiRequest aligoApi = new ApiRequest(aligoUrl, "POST");
        aligoApi.putRequestBody(new BasicNameValuePair("key",aligoKey));
        aligoApi.putRequestBody(new BasicNameValuePair("user_id",aligoIdentifier));
        aligoApi.putRequestBody(new BasicNameValuePair("sender",aligoSender));
        return aligoApi;
    }

    private Boolean alertIfFailure(Map<String,Object> res) {
       if(res.containsKey("error_cnt") && res.get("error_cnt")!=null && !res.get("error_cnt").equals(0)){
            ApiRequest aligoApi = setAligoDefaultRequest();
            aligoApi.putRequestBody(new BasicNameValuePair("receiver","01073356712"));
            aligoApi.putRequestBody(new BasicNameValuePair("msg","aligo failure "+res.get("msg_id")));
            System.out.println(aligoApi.getResponseText());
            return false;
        }
       return true;
    }


}
