package com.zk.openrs.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Order implements Serializable {
    int id;
    int productId;
    String formId;
    int rentalTime;
    String openId;
    Date createTime;
    String completeFlag;

    public Order(int productId, String formId, int rentalTime, String openId, Date createTime,String completeFlag) {
        this.productId = productId;
        this.formId = formId;
        this.rentalTime = rentalTime;
        this.openId = openId;
        this.createTime = createTime;
        this.completeFlag=completeFlag;
    }

    public Order(int id, int productId, String formId, int rentalTime, String openId, Date createTime,String completeFlag) {
        this.id = id;
        this.productId = productId;
        this.formId = formId;
        this.rentalTime = rentalTime;
        this.openId = openId;
        this.createTime = createTime;
        this.completeFlag=completeFlag;
    }
}
