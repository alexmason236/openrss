package com.zk.openrs.pojo;

import lombok.Data;

@Data
public class BuyPojo {
    int goodsId;
    int number;

    public BuyPojo(int goodsId, int number) {
        this.goodsId = goodsId;
        this.number = number;
    }
}
