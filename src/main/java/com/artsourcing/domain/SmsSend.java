package com.artsourcing.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="SDK_SMS_SEND")
public class SmsSend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long msgId;

    @Column(length = 200)
    String smsMsg;

    @Column(columnDefinition = "TEXT")
    String destInfo;

}
