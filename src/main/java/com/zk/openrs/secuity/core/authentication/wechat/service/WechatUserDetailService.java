package com.zk.openrs.secuity.core.authentication.wechat.service;

import com.zk.openrs.mapper.UserMapper;
import com.zk.openrs.pojo.WechatUser;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class WechatUserDetailService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String openid) throws UsernameNotFoundException {
        WechatUser wechatUser=userMapper.getByOpenId(openid);
        if (wechatUser==null) throw new UsernameNotFoundException("openId not found");
        return  new WechatUserDetails(wechatUser.getNickName(),wechatUser.getPassword(),wechatUser.getOpenId(),wechatUser.getAccPoint(),wechatUser.getGender(),wechatUser.getCity(),wechatUser.getProvince(),AuthorityUtils.NO_AUTHORITIES);
    }
}
