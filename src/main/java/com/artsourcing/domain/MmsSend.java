package com.artsourcing.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="SDK_MMS_SEND")
public class MmsSend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long msgId;

    @Column(columnDefinition = "TEXT")
    String mmsMsg;

    @Column(columnDefinition = "TEXT")
    String destInfo;

}
