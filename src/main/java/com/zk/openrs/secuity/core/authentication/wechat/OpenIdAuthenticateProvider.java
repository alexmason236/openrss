package com.zk.openrs.secuity.core.authentication.wechat;

import com.zk.openrs.pojo.WechatUser;
import com.zk.openrs.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

public class OpenIdAuthenticateProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Resource
    private UserService userService;

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OpenIdAuthenticationToken openIdAuthenticationToken=(OpenIdAuthenticationToken) authentication;
        UserDetails user=userDetailsService.loadUserByUsername((String) openIdAuthenticationToken.getPrincipal());

        OpenIdAuthenticationToken authenticationResult=new OpenIdAuthenticationToken(user,user.getAuthorities());
        authenticationResult.setDetails(openIdAuthenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
         return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
