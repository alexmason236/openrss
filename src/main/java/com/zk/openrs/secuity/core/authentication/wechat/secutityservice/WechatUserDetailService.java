package com.zk.openrs.secuity.core.authentication.wechat.secutityservice;

import com.zk.openrs.mapper.UserMapper;
import com.zk.openrs.pojo.WechatUser;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class WechatUserDetailService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String openid) throws UsernameNotFoundException {
        WechatUser wechatUser=userMapper.getByOpenId(openid);
        if (wechatUser==null) throw new UnapprovedClientAuthenticationException("openId not find");
        return  new User()
        if (wechatUser==null){
            userMapper.addUser(new WechatUser(openid,0,"123","微信用户"));
            return new User(openid,"123", AuthorityUtils.NO_AUTHORITIES);
        }else {
            return new User(wechatUser.getOpenId(),wechatUser.getPassword(), AuthorityUtils.NO_AUTHORITIES);
        }
    }
}
