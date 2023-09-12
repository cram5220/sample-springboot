package com.artsourcing.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.net.URLDecoder;

@Getter
public class SmsRequestDto {
    String receiver;
    String msg;

    @Builder
    SmsRequestDto(String receiver, String msg) {
        this.receiver = receiver;
        this.msg = URLDecoder.decode(msg);
    }
}
