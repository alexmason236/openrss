package com.zk.openrs.secuity.core.authentication.wechat.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class WechatUserDetails extends User {
    String openId;
    float accPoint;
    String gender;
    String city;
    String province;
    String nickName;

    public WechatUserDetails(String openId, String password,String nickName,float accPoint,String gender,String city, String province,Collection<? extends GrantedAuthority> authorities) {
        super(openId, password, authorities);
        this.accPoint=accPoint;
        this.city=city;
        this.gender=gender;
        this.openId=openId;
        this.province=province;
        this.nickName=nickName;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
