package com.zk.openrs.service;

import com.zk.openrs.mapper.UserMapper;
import com.zk.openrs.pojo.WechatUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserService {
    @Resource
    private UserMapper userMapper;

    public void addUser(WechatUser wechatUser) {
        userMapper.addUser(wechatUser);
    }

    public WechatUser getByOpenId(String openId){
        return  userMapper.getByOpenId(openId);
    }

    public void updateUserAccPoint(float accPoint, String openId) {
        userMapper.updateUserAccPoint(accPoint,openId);
    }

}
