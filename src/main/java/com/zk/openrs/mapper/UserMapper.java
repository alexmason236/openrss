package com.zk.openrs.mapper;

import com.zk.openrs.pojo.WechatUser;

public interface UserMapper {
    void addUser(WechatUser wechatUser);
    WechatUser getByOpenId(String openid);
}
