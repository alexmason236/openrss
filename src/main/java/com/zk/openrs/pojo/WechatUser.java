package com.zk.openrs.pojo;

import java.io.Serializable;

public class WechatUser implements Serializable {
    int id;
    String openId;
    float accPoint;
    String password;
    String nickName;
    String gender;
    String city;
    String province;

    public WechatUser() {
    }

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public float getAccPoint() {
        return accPoint;
    }

    public void setAccPoint(float accPoint) {
        this.accPoint = accPoint;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
