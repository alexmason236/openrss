package com.zk.openrs.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReceivedMobileData implements Serializable {
    String mobilePhoneNumber;
    String messageContent;
}
