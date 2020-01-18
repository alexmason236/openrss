package com.zk.openrs.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WechatUser implements Serializable {
    int id;
    String openId;
    float accPoint;
    String password;
    String nickName;
    String gender;
    String city;
    String province;


    public WechatUser(String openId, float accPoint, String password, String nickName, String gender, String city, String province) {
        this.openId = openId;
        this.accPoint = accPoint;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.city = city;
        this.province = province;
    }

    public WechatUser(int id, String openId, float accPoint, String password, String nickName, String gender, String city, String province) {
        this.id = id;
        this.openId = openId;
        this.accPoint = accPoint;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.city = city;
        this.province = province;
    }

}
